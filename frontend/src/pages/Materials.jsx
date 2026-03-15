import { useEffect, useState } from 'react';
import { getMaterials, createMaterial, updateMaterial, deleteMaterial, uploadMaterialImage, imgUrl } from '../api';
import CurrencyInput from '../components/CurrencyInput';

const EMPTY = { name: '', color: '', roll: '', origin: '', price: '', quantity: '', unit: 'm' };
const fmt = (n) => n != null ? Number(n).toLocaleString('vi-VN') : '0';
const PAGE_SIZE = 10;

const FABRIC_COLORS = [
  { label: 'Tất cả màu', value: '' },
  { label: '⚪ Trắng', value: 'Trắng' },
  { label: '⚫ Đen', value: 'Đen' },
  { label: '🔵 Xanh', value: 'Xanh' },
  { label: '🔴 Đỏ', value: 'Đỏ' },
  { label: '🟤 Nâu', value: 'Nâu' },
  { label: '🟡 Vàng', value: 'Vàng' },
  { label: '🟣 Tím', value: 'Tím' },
  { label: '🟢 Xanh lá', value: 'Xanh lá' },
];

const Pagination = ({ page, total, perPage, onChange }) => {
  const pages = Math.ceil(total / perPage);
  if (pages <= 1) return null;
  return (
    <div className="pagination">
      <button className="pag-btn" onClick={() => onChange(page - 1)} disabled={page === 1}>‹</button>
      {Array.from({ length: pages }, (_, i) => i + 1).map(p => (
        <button key={p} className={`pag-btn ${p === page ? 'active' : ''}`} onClick={() => onChange(p)}>{p}</button>
      ))}
      <button className="pag-btn" onClick={() => onChange(page + 1)} disabled={page === pages}>›</button>
    </div>
  );
};

export default function Materials() {
  const [list, setList]         = useState([]);
  const [modal, setModal]       = useState(null);
  const [form, setForm]         = useState(EMPTY);
  const [loading, setLoading]   = useState(true);
  const [toast, setToast]       = useState('');
  const [page, setPage]         = useState(1);
  const [search, setSearch]     = useState('');
  const [colorFilter, setColorFilter] = useState('');

  const load = () => getMaterials().then(r => { setList(r.data); setLoading(false); });
  useEffect(() => { load(); }, []);

  const showToast = (m) => { setToast(m); setTimeout(() => setToast(''), 2500); };
  const F = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!confirm('Xác nhận lưu thông tin kho vải?')) return;
    const d = { ...form, price: Number(form.price), quantity: Number(form.quantity) };
    try {
      if (modal === 'create') { await createMaterial(d); showToast('Đã nhập vải mới'); }
      else { await updateMaterial(modal.id, d); showToast('Đã cập nhật'); }
      setModal(null); load();
    } catch { showToast('Lỗi xảy ra'); }
  };

  const handleImageUpload = async (matId, file) => {
    if (!file) return;
    try {
      await uploadMaterialImage(matId, file);
      load(); showToast('Đã cập nhật ảnh vải');
    } catch { showToast('Lỗi tải ảnh'); }
  };

  const [stockModal, setStockModal] = useState(null);
  const [addQty, setAddQty] = useState('');

  const handleAddStock = async (e) => {
    e.preventDefault();
    if (!addQty || isNaN(addQty)) return;
    const newQty = (stockModal.quantity || 0) + Number(addQty);
    try {
      await updateMaterial(stockModal.id, { ...stockModal, quantity: newQty });
      showToast(`Đã thêm ${addQty} ${stockModal.unit || 'm'}`);
      setStockModal(null); setAddQty(''); load();
    } catch { showToast('Lỗi khi cập nhật kho'); }
  };

  /* Client-side filter */
  const filtered = list.filter(m => {
    const matchSearch = !search || m.name?.toLowerCase().includes(search.toLowerCase()) || m.roll?.toLowerCase().includes(search.toLowerCase());
    const matchColor  = !colorFilter || (m.color || '').toLowerCase().includes(colorFilter.toLowerCase());
    return matchSearch && matchColor;
  });

  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <>
      <div className="page-hdr">
        <h2>Kho vải</h2>
        <button className="btn btn-primary" onClick={() => { setForm(EMPTY); setModal('create'); }}>+ Nhập vải mới</button>
      </div>

      {/* Filters */}
      <div className="card" style={{ marginBottom: 20, padding: '14px 20px' }}>
        <div style={{ display: 'flex', gap: 12, flexWrap: 'wrap', alignItems: 'center' }}>
          <input className="form-input" placeholder="Tìm tên vải hoặc mã quyển…"
            style={{ flex: 2, minWidth: 180 }}
            value={search} onChange={e => { setSearch(e.target.value); setPage(1); }} />
          <select className="form-input" style={{ flex: 1, minWidth: 160 }}
            value={colorFilter} onChange={e => { setColorFilter(e.target.value); setPage(1); }}>
            {FABRIC_COLORS.map(c => <option key={c.value} value={c.value}>{c.label}</option>)}
          </select>
        </div>
      </div>

      {loading ? (
        <div className="spinner-wrap"><div className="spinner" /></div>
      ) : filtered.length === 0 ? (
        <div className="empty-state"><div className="icon">🧵</div>Không tìm thấy loại vải nào</div>
      ) : (
        <>
          <div className="material-grid">
            {paged.map(m => (
              <div key={m.id} className="material-card" onClick={() => setModal({ type: 'view', item: m })}>
                {/* Fabric image */}
                <div className="mat-img">
                  {m.imageUrl
                    ? <img src={imgUrl(m.imageUrl)} alt={m.name} />
                    : <span style={{ fontSize: 32 }}>🧵</span>}
                  {/* Upload overlay */}
                  <label className="mat-img-overlay" onClick={e => e.stopPropagation()}>
                    📷
                    <input type="file" hidden accept="image/*" onChange={e => handleImageUpload(m.id, e.target.files[0])} />
                  </label>
                </div>
                <div className="mat-info">
                  <div className="mat-name">{m.name}</div>
                  <div className="mat-color">{m.color || '—'}</div>
                  {m.roll && <div style={{ fontSize: 13, color: 'var(--text-muted)', marginTop: 2 }}>Mã: {m.roll}</div>}
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: 10 }}>
                    <span 
                      className={`status-pill ${m.quantity <= 10 ? 's-cancel' : 's-done'}`} 
                      style={{ fontSize: 13, cursor: 'pointer' }}
                      onClick={(e) => { e.stopPropagation(); setStockModal(m); }}
                      title="Bấm để nhập thêm"
                    >
                      {m.quantity} {m.unit || 'm'} +
                    </span>
                    <button className="btn btn-ghost btn-xs" style={{ fontSize: 13 }} 
                      onClick={(e) => { e.stopPropagation(); setForm(m); setModal({ type: 'edit', item: m }); }}>Sửa</button>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <Pagination page={page} total={filtered.length} perPage={PAGE_SIZE} onChange={setPage} />
        </>
      )}

      {/* Stock Modal */}
      {stockModal && (
        <div className="modal-overlay" onClick={() => setStockModal(null)}>
          <div className="modal modal-sm" onClick={e => e.stopPropagation()} style={{ maxWidth: 400 }}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>Nhập thêm: {stockModal.name}</h3>
              <button className="modal-close" onClick={() => setStockModal(null)}>✕</button>
            </div>
            <form onSubmit={handleAddStock}>
              <div className="modal-body">
                <p style={{ marginBottom: 16, fontSize: 15, color: 'var(--text-muted)' }}>
                  Tồn hiện tại: <strong>{stockModal.quantity} {stockModal.unit}</strong>
                </p>
                <div className="form-group">
                  <label className="form-label">Số lượng nhập thêm ({stockModal.unit})</label>
                  <input className="form-input" type="number" step="0.1" autoFocus required
                    value={addQty} onChange={e => setAddQty(e.target.value)} placeholder="0.0" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-ghost" onClick={() => setStockModal(null)}>Huỷ</button>
                <button type="submit" className="btn btn-primary">Xác nhận cộng</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {modal && (
        <div className="modal-overlay" onClick={() => setModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>
                {modal === 'create' ? 'Nhập vải mới' : (modal.type === 'view' ? 'Chi tiết vải' : 'Cập nhật vải')}
              </h3>
              <button className="modal-close" onClick={() => setModal(null)}>✕</button>
            </div>

            {modal.type === 'view' ? (
              <div className="modal-body">
                <div style={{ display: 'flex', gap: 24, marginBottom: 20 }}>
                  <div style={{ width: 140, height: 180, borderRadius: 12, background: 'var(--bg-warm)', display: 'flex', alignItems: 'center', justifyContent: 'center', overflow: 'hidden', border: '1px solid var(--border)' }}>
                    {modal.item.imageUrl 
                      ? <img src={imgUrl(modal.item.imageUrl)} style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
                      : <span style={{ fontSize: 48 }}>🧵</span>
                    }
                  </div>
                  <div style={{ flex: 1 }}>
                    <h4 style={{ fontFamily: 'var(--serif)', fontSize: 28, marginBottom: 8 }}>{modal.item.name}</h4>
                    <div style={{ fontSize: 18, color: 'var(--accent)', fontWeight: 600, marginBottom: 4 }}>{modal.item.color}</div>
                    <div style={{ fontSize: 16, color: 'var(--text-muted)' }}>Mã quyển: {modal.item.roll || '—'}</div>
                    <div style={{ fontSize: 16, color: 'var(--text-muted)', marginTop: 4 }}>Xuất xứ: {modal.item.origin || '—'}</div>
                    
                    <div style={{ marginTop: 20, display: 'flex', gap: 12 }}>
                      <div style={{ flex: 1, padding: 12, background: 'var(--bg-warm)', borderRadius: 10 }}>
                        <div className="stat-label">Đơn giá</div>
                        <div style={{ fontWeight: 700, fontSize: 18 }}>{modal.item.price?.toLocaleString('vi-VN')} ₫</div>
                      </div>
                      <div style={{ flex: 1, padding: 12, background: 'var(--bg-warm)', borderRadius: 10 }}>
                        <div className="stat-label">Tồn kho</div>
                        <div style={{ fontWeight: 700, fontSize: 18, color: modal.item.quantity <= 10 ? 'var(--danger)' : 'var(--success)' }}>
                          {modal.item.quantity} {modal.item.unit}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="modal-footer">
                  <button className="btn btn-ghost" onClick={() => setModal(null)}>Đóng</button>
                  <button className="btn btn-primary" onClick={() => { setForm(modal.item); setModal({ type: 'edit', item: modal.item }); }}>Chỉnh sửa</button>
                </div>
              </div>
            ) : (
              <form onSubmit={handleSubmit}>
                <div className="modal-body">
                  <div className="form-row">
                    <div className="form-group">
                      <label className="form-label">Tên vải *</label>
                      <input className="form-input" required value={form.name} onChange={F('name')} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Màu sắc</label>
                      <input className="form-input" value={form.color} onChange={F('color')} />
                    </div>
                  </div>
                  <div className="form-row">
                    <div className="form-group">
                      <label className="form-label">Mã quyển</label>
                      <input className="form-input" value={form.roll} onChange={F('roll')} />
                    </div>
                    <div className="form-group">
                      <label className="form-label">Xuất xứ</label>
                      <input className="form-input" value={form.origin} onChange={F('origin')} />
                    </div>
                  </div>
                  <div className="form-row">
                    <CurrencyInput
                      label="Giá nhập (₫/đvị)"
                      value={form.price}
                      onChange={val => setForm(f => ({ ...f, price: val }))}
                      style={{ flex: 1 }}
                    />
                    <div className="form-group">
                      <label className="form-label">Số lượng tồn</label>
                      <input className="form-input" type="number" step="0.1" value={form.quantity} onChange={F('quantity')} />
                    </div>
                  </div>
                  <div className="form-group">
                    <label className="form-label">Đơn vị</label>
                    <select className="form-input" value={form.unit} onChange={F('unit')}>
                      <option value="m">Mét (m)</option>
                      <option value="kg">Kilogram (kg)</option>
                      <option value="yard">Yard</option>
                    </select>
                  </div>
                </div>
                <div className="modal-footer">
                  <button type="button" className="btn btn-ghost" onClick={() => setModal(null)}>Huỷ</button>
                  <button type="submit" className="btn btn-primary">Lưu</button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
