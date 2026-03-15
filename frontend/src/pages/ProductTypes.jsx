import { useEffect, useState } from 'react';
import { getProductTypes, createProductType, updateProductType,
         getMeasurementTypes, createMeasurementType, uploadProductTypeImage, imgUrl } from '../api';

const EMPTY_PT = { name: '', code: '' };

const LIB_DEFAULT = [
  { name: 'Dài áo',     bodyPart: 'Thân trên' },
  { name: 'Vòng ngực',  bodyPart: 'Thân trên' },
  { name: 'Ngang vai',  bodyPart: 'Thân trên' },
  { name: 'Vòng cổ',   bodyPart: 'Thân trên' },
  { name: 'Vòng eo',   bodyPart: 'Thân trên' },
  { name: 'Hạ ngực',   bodyPart: 'Thân trên' },
  { name: 'Dài tay',   bodyPart: 'Tay'       },
  { name: 'Bắp tay',   bodyPart: 'Tay'       },
  { name: 'Cửa tay',   bodyPart: 'Tay'       },
  { name: 'Dài quần',  bodyPart: 'Thân dưới' },
  { name: 'Vòng bụng', bodyPart: 'Thân dưới' },
  { name: 'Vòng mông', bodyPart: 'Thân dưới' },
  { name: 'Vòng đùi',  bodyPart: 'Thân dưới' },
  { name: 'Ống quần',  bodyPart: 'Thân dưới' },
];
const ALL_PARTS = ['Thân trên', 'Tay', 'Thân dưới', 'Khác'];
const PAGE_SIZE = 10;

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

export default function ProductTypes() {
  const [types, setTypes]         = useState([]);
  const [editModal, setEditModal] = useState(null);
  const [detailModal, setDetailModal] = useState(null);
  const [detailMt, setDetailMt]   = useState([]);
  const [formPt, setFormPt]       = useState(EMPTY_PT);
  const [lib, setLib]             = useState(LIB_DEFAULT);
  const [selected, setSelected]   = useState([]);
  const [toast, setToast]         = useState('');
  const [newM, setNewM]           = useState({ name: '', bodyPart: 'Thân trên' });
  const [showNewM, setShowNewM]   = useState(false);
  const [partFilter, setPartFilter] = useState('');
  const [page, setPage]           = useState(1);
  const [search, setSearch]       = useState('');

  const load = () => getProductTypes().then(r => setTypes(r.data));
  useEffect(() => { load(); }, []);

  const showToast = (m) => { setToast(m); setTimeout(() => setToast(''), 2500); };

  const openDetail = async (pt) => {
    const r = await getMeasurementTypes(pt.id);
    setDetailMt(r.data);
    setDetailModal(pt);
  };

  const openCreate = () => { setFormPt(EMPTY_PT); setSelected([]); setEditModal('create'); };
  const openEdit   = async (pt) => {
    setFormPt(pt);
    const r = await getMeasurementTypes(pt.id);
    setSelected(r.data.map(m => ({ name: m.name, bodyPart: m.bodyPart })));
    setEditModal(pt);
  };

  const toggle = (m) => {
    setSelected(prev => prev.find(x => x.name === m.name)
      ? prev.filter(x => x.name !== m.name)
      : [...prev, m]);
  };

  const addToLib = () => {
    if (!newM.name.trim()) return;
    setLib(prev => [...prev, { ...newM }]);
    setNewM({ name: '', bodyPart: 'Thân trên' });
    setShowNewM(false);
  };

  const handleImageUpload = async (ptId, file) => {
    if (!file) return;
    try {
      await uploadProductTypeImage(ptId, file);
      load(); showToast('Đã cập nhật ảnh');
    } catch { showToast('Lỗi tải ảnh'); }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!confirm('Xác nhận lưu cấu hình?')) return;
    try {
      let ptId;
      if (editModal === 'create') {
        const r = await createProductType(formPt);
        ptId = r.data.id;
        for (const m of selected) {
          await createMeasurementType({ name: m.name, bodyPart: m.bodyPart, unit: 'cm', productTypeId: ptId });
        }
      } else {
        await updateProductType(editModal.id, formPt);
        ptId = editModal.id;
        const r2 = await getMeasurementTypes(ptId);
        const existing = r2.data.map(x => x.name);
        for (const m of selected) {
          if (!existing.includes(m.name)) {
            await createMeasurementType({ name: m.name, bodyPart: m.bodyPart, unit: 'cm', productTypeId: ptId });
          }
        }
      }
      setEditModal(null); load(); showToast('Đã lưu!');
    } catch { showToast('Lỗi xảy ra'); }
  };

  const libFiltered = partFilter ? lib.filter(m => m.bodyPart === partFilter) : lib;
  const searchFiltered = types.filter(pt =>
    !search || pt.name.toLowerCase().includes(search.toLowerCase()) || (pt.code || '').toLowerCase().includes(search.toLowerCase())
  );
  const paged = searchFiltered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <>
      <div className="page-hdr">
        <h2>Thiết lập sản phẩm</h2>
        <button className="btn btn-primary" onClick={openCreate}>+ Loại sản phẩm mới</button>
      </div>

      {/* Search */}
      <div className="card" style={{ marginBottom: 20, padding: '12px 20px' }}>
        <input className="form-input" placeholder="Tìm loại sản phẩm…"
          value={search} onChange={e => { setSearch(e.target.value); setPage(1); }} />
      </div>

      {paged.length === 0 ? (
        <div className="empty-state"><div className="icon">👔</div>Không tìm thấy loại sản phẩm</div>
      ) : (
        <>
          <div className="pt-grid">
            {paged.map(pt => (
              <div key={pt.id} className="pt-card" onClick={() => openDetail(pt)}>
                <div className="pt-img">
                  {pt.imageUrl ? <img src={imgUrl(pt.imageUrl)} alt={pt.name} /> : '👔'}
                </div>
                <div className="pt-info">
                  <div className="pt-name">{pt.name}</div>
                  <div className="pt-code">{pt.code || '—'}</div>
                  <div className="pt-actions" onClick={e => e.stopPropagation()}>
                    <button className="btn btn-ghost btn-xs" onClick={() => openEdit(pt)}>Sửa</button>
                    <label className="btn btn-ghost btn-xs" style={{ cursor: 'pointer' }}>
                      🖼
                      <input type="file" hidden accept="image/*"
                        onChange={e => handleImageUpload(pt.id, e.target.files[0])} />
                    </label>
                  </div>
                </div>
              </div>
            ))}
          </div>
          <Pagination page={page} total={searchFiltered.length} perPage={PAGE_SIZE} onChange={setPage} />
        </>
      )}

      {/* Detail popup */}
      {detailModal && (
        <div className="modal-overlay" onClick={() => setDetailModal(null)}>
          <div className="modal" style={{ maxWidth: 520 }} onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>{detailModal.name}</h3>
              <button className="modal-close" onClick={() => setDetailModal(null)}>✕</button>
            </div>
            <div className="modal-body">
              {detailModal.imageUrl && (
                <img src={imgUrl(detailModal.imageUrl)} alt={detailModal.name}
                  style={{ width: '100%', height: 220, objectFit: 'cover', borderRadius: 12, marginBottom: 20 }} />
              )}
              <div style={{ display: 'flex', gap: 16, marginBottom: 20 }}>
                <div style={{ flex: 1, textAlign: 'center', padding: 16, background: 'var(--bg-warm)', borderRadius: 10 }}>
                  <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 6 }}>Mã ký hiệu</div>
                  <div style={{ fontFamily: 'var(--serif)', fontSize: 22, fontWeight: 700 }}>{detailModal.code || '—'}</div>
                </div>
                <div style={{ flex: 1, textAlign: 'center', padding: 16, background: 'var(--bg-warm)', borderRadius: 10 }}>
                  <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: '0.1em', color: 'var(--text-muted)', marginBottom: 6 }}>Thông số đo</div>
                  <div style={{ fontFamily: 'var(--serif)', fontSize: 22, fontWeight: 700 }}>{detailMt.length}</div>
                </div>
              </div>
              <div className="card-title" style={{ fontSize: 14, marginBottom: 12 }}>Danh sách thông số</div>
              {detailMt.length === 0 ? (
                <div style={{ color: 'var(--text-muted)', fontStyle: 'italic', fontSize: 15 }}>Chưa cài thông số đo</div>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(130px, 1fr))', gap: 8 }}>
                  {detailMt.map(mt => (
                    <div key={mt.id} style={{ padding: '10px 12px', border: '1px solid var(--border)', borderRadius: 8, textAlign: 'center' }}>
                      <div style={{ fontSize: 14, fontWeight: 700 }}>{mt.name}</div>
                      <div style={{ fontSize: 11, color: 'var(--text-muted)', marginTop: 2 }}>{mt.bodyPart}</div>
                    </div>
                  ))}
                </div>
              )}
            </div>
            <div className="modal-footer">
              <button className="btn btn-ghost" onClick={() => setDetailModal(null)}>Đóng</button>
              <button className="btn btn-primary" onClick={() => { openEdit(detailModal); setDetailModal(null); }}>✎ Sửa</button>
            </div>
          </div>
        </div>
      )}

      {/* Create/Edit Modal */}
      {editModal && (
        <div className="modal-overlay" onClick={() => setEditModal(null)}>
          <div className="modal" style={{ maxWidth: 860 }} onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>{editModal === 'create' ? 'Tạo loại sản phẩm' : 'Cập nhật: ' + formPt.name}</h3>
              <button className="modal-close" onClick={() => setEditModal(null)}>✕</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-row" style={{ marginBottom: 28 }}>
                  <div className="form-group">
                    <label className="form-label">Tên sản phẩm *</label>
                    <input className="form-input" required value={formPt.name}
                      onChange={e => setFormPt(f => ({ ...f, name: e.target.value }))} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Mã ký hiệu</label>
                    <input className="form-input" value={formPt.code}
                      onChange={e => setFormPt(f => ({ ...f, code: e.target.value }))} placeholder="ASM" />
                  </div>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: 12, marginBottom: 20 }}>
                  <hr style={{ flex: 1, border: 'none', borderTop: '1px solid var(--border)' }} />
                  <span className="section-label" style={{ margin: 0 }}>Chọn thông số đo</span>
                  <hr style={{ flex: 1, border: 'none', borderTop: '1px solid var(--border)' }} />
                </div>

                <div style={{ display: 'grid', gridTemplateColumns: '1.3fr 1fr', gap: 24 }}>
                  <div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 12 }}>
                      <span className="section-label" style={{ margin: 0 }}>Thư viện</span>
                      <button type="button" className="btn btn-ghost btn-xs" onClick={() => setShowNewM(v => !v)}>
                        {showNewM ? 'Đóng' : '+ Thêm mới'}
                      </button>
                    </div>

                    {showNewM && (
                      <div style={{ padding: 14, background: 'var(--bg-warm)', borderRadius: 10, marginBottom: 12, display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                        <input className="form-input" placeholder="Tên thông số…" style={{ flex: 2, minWidth: 140, height: 44 }}
                          value={newM.name} onChange={e => setNewM(n => ({ ...n, name: e.target.value }))} />
                        <select className="form-input" style={{ flex: 1, minWidth: 120, height: 44 }}
                          value={newM.bodyPart} onChange={e => setNewM(n => ({ ...n, bodyPart: e.target.value }))}>
                          {ALL_PARTS.map(p => <option key={p}>{p}</option>)}
                        </select>
                        <button type="button" className="btn btn-accent btn-sm" onClick={addToLib}>Thêm</button>
                      </div>
                    )}

                    <div style={{ display: 'flex', gap: 6, marginBottom: 10, flexWrap: 'wrap' }}>
                      {['', ...ALL_PARTS].map(p => (
                        <button key={p} type="button"
                          className={`btn btn-xs ${partFilter === p ? 'btn-primary' : 'btn-ghost'}`}
                          onClick={() => setPartFilter(p)}>{p || 'Tất cả'}</button>
                      ))}
                    </div>

                    <div className="picker-grid" style={{ maxHeight: 300, overflowY: 'auto' }}>
                      {libFiltered.map(m => (
                        <div key={m.name}
                          className={`picker-item ${selected.find(x => x.name === m.name) ? 'on' : ''}`}
                          onClick={() => toggle(m)}>
                          <strong>{m.name}</strong>
                          <span>{m.bodyPart}</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div style={{ borderLeft: '1px solid var(--border)', paddingLeft: 24 }}>
                    <span className="section-label">Đã chọn ({selected.length})</span>
                    {selected.length === 0 ? (
                      <div style={{ color: 'var(--text-muted)', fontStyle: 'italic', padding: '14px 0', fontSize: 15 }}>Chưa có thông số</div>
                    ) : (
                      <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                        {selected.map(m => (
                          <div key={m.name} style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '10px 14px', background: 'var(--accent-light)', borderRadius: 8, border: '1px solid var(--accent)' }}>
                            <div>
                              <div style={{ fontWeight: 700, fontSize: 14 }}>{m.name}</div>
                              <div style={{ fontSize: 12, color: 'var(--text-muted)' }}>{m.bodyPart}</div>
                            </div>
                            <button type="button" style={{ background: 'none', border: 'none', cursor: 'pointer', color: 'var(--text-muted)', fontSize: 18 }}
                              onClick={() => toggle(m)}>✕</button>
                          </div>
                        ))}
                      </div>
                    )}
                  </div>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-ghost" onClick={() => setEditModal(null)}>Huỷ</button>
                <button type="submit" className="btn btn-primary">Xác nhận lưu</button>
              </div>
            </form>
          </div>
        </div>
      )}
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
