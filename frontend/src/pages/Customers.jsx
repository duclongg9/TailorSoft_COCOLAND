import { useEffect, useState } from 'react';
import { getCustomers, createCustomer, updateCustomer } from '../api';

const EMPTY = { name: '', phone: '', email: '', address: '', gender: 'Nam' };
const PAGE_SIZE = 9;
const avatarFor = (g) => g === 'Nữ' ? '/avatar_female.png' : '/avatar_male.png';
const titleFor  = (g) => g === 'Nữ' ? 'Mrs.' : 'Mr.';

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

export default function Customers() {
  const [list, setList] = useState([]);
  const [search, setSearch] = useState('');
  const [editModal, setEditModal]   = useState(null);
  const [detailModal, setDetailModal] = useState(null);
  const [form, setForm]   = useState(EMPTY);
  const [loading, setLoading] = useState(true);
  const [toast, setToast] = useState('');
  const [page, setPage]   = useState(1);

  const load = (q = '') => getCustomers(q).then(r => { setList(r.data); setLoading(false); setPage(1); });
  useEffect(() => { load(); }, []);

  const showToast = (m) => { setToast(m); setTimeout(() => setToast(''), 2500); };
  const F = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!confirm('Xác nhận lưu thông tin?')) return;
    try {
      if (editModal === 'create') { await createCustomer(form); showToast('Đã thêm khách hàng'); }
      else { await updateCustomer(editModal.id, form); showToast('Đã cập nhật'); }
      setEditModal(null); setDetailModal(null); load(search);
    } catch { showToast('Lỗi xảy ra'); }
  };

  const paged = list.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <>
      <div className="page-hdr">
        <h2>Khách hàng</h2>
        <button className="btn btn-primary" onClick={() => { setForm(EMPTY); setEditModal('create'); }}>+ Thêm khách</button>
      </div>

      <div className="card" style={{ marginBottom: 24, padding: '14px 20px' }}>
        <input className="form-input" placeholder="Tìm theo tên hoặc số điện thoại…"
          value={search}
          onChange={e => { setSearch(e.target.value); load(e.target.value); }} />
      </div>

      {loading ? (
        <div className="spinner-wrap"><div className="spinner" /></div>
      ) : list.length === 0 ? (
        <div className="empty-state"><div className="icon">👤</div>Không tìm thấy khách hàng</div>
      ) : (
        <>
          <div className="customer-grid">
            {paged.map(c => (
              <div key={c.id} className="customer-card" onClick={() => setDetailModal(c)}>
                <img className="avatar" src={avatarFor(c.gender)} alt={c.gender || 'Nam'} />
                <div>
                  <div className="c-name">{titleFor(c.gender)} {c.name}</div>
                  <div className="c-phone">{c.phone || '—'}</div>
                </div>
                <div className="c-actions" onClick={e => e.stopPropagation()}>
                  <button className="btn btn-ghost btn-sm" onClick={() => { setForm(c); setEditModal(c); }}>Sửa</button>
                </div>
              </div>
            ))}
          </div>
          <Pagination page={page} total={list.length} perPage={PAGE_SIZE} onChange={setPage} />
        </>
      )}

      {/* Detail modal */}
      {detailModal && (
        <div className="modal-overlay" onClick={() => setDetailModal(null)}>
          <div className="modal" style={{ maxWidth: 440 }} onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>{titleFor(detailModal.gender)} {detailModal.name}</h3>
              <button className="modal-close" onClick={() => setDetailModal(null)}>✕</button>
            </div>
            <div className="modal-body">
              <div style={{ display: 'flex', gap: 24, alignItems: 'center', marginBottom: 24 }}>
                <img src={avatarFor(detailModal.gender)} alt="" style={{ width: 88, height: 88, borderRadius: '50%', border: '4px solid var(--accent-light)', objectFit: 'cover' }} />
                <div>
                  <div style={{ fontFamily: 'var(--serif)', fontSize: 24, fontWeight: 700 }}>{titleFor(detailModal.gender)} {detailModal.name}</div>
                  <div style={{ color: 'var(--accent)', fontWeight: 700, fontSize: 18, marginTop: 4 }}>{detailModal.phone}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: 14 }}>{detailModal.email}</div>
                </div>
              </div>
              {[
                { label: 'Giới tính', val: detailModal.gender || 'Nam' },
                { label: 'Địa chỉ',  val: detailModal.address || '—' },
              ].map(({ label, val }) => (
                <div key={label} style={{ padding: '14px 0', borderTop: '1px solid var(--border)' }}>
                  <div style={{ fontSize: 12, fontWeight: 700, letterSpacing: '0.1em', textTransform: 'uppercase', color: 'var(--text-muted)', marginBottom: 4 }}>{label}</div>
                  <div style={{ fontSize: 16 }}>{val}</div>
                </div>
              ))}
            </div>
            <div className="modal-footer">
              <button className="btn btn-ghost" onClick={() => setDetailModal(null)}>Đóng</button>
              <button className="btn btn-primary" onClick={() => { setForm(detailModal); setEditModal(detailModal); setDetailModal(null); }}>✎ Sửa thông tin</button>
            </div>
          </div>
        </div>
      )}

      {/* Edit modal */}
      {editModal && (
        <div className="modal-overlay" onClick={() => setEditModal(null)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>{editModal === 'create' ? 'Thêm khách hàng' : 'Sửa thông tin'}</h3>
              <button className="modal-close" onClick={() => setEditModal(null)}>✕</button>
            </div>
            <form onSubmit={handleSubmit}>
              <div className="modal-body">
                <div className="form-group">
                  <label className="form-label">Họ tên *</label>
                  <input className="form-input" required value={form.name} onChange={F('name')} />
                </div>
                <div className="form-group">
                  <label className="form-label">Giới tính</label>
                  <div style={{ display: 'flex', gap: 16, marginTop: 6 }}>
                    {['Nam', 'Nữ'].map(g => (
                      <label key={g} style={{ display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer', padding: '10px 18px', border: `2px solid ${form.gender === g ? 'var(--accent)' : 'var(--border)'}`, borderRadius: 12, background: form.gender === g ? 'var(--accent-light)' : '#fff', transition: '.2s' }}>
                        <img src={avatarFor(g)} alt={g} style={{ width: 36, height: 36, borderRadius: '50%', objectFit: 'cover' }} />
                        <input type="radio" hidden value={g} checked={form.gender === g} onChange={F('gender')} />
                        <span style={{ fontWeight: 600, fontSize: 16 }}>{g}</span>
                      </label>
                    ))}
                  </div>
                </div>
                <div className="form-row">
                  <div className="form-group">
                    <label className="form-label">Số điện thoại</label>
                    <input className="form-input" value={form.phone} onChange={F('phone')} />
                  </div>
                  <div className="form-group">
                    <label className="form-label">Email</label>
                    <input className="form-input" type="email" value={form.email} onChange={F('email')} />
                  </div>
                </div>
                <div className="form-group">
                  <label className="form-label">Địa chỉ</label>
                  <textarea className="form-input" rows={3} value={form.address} onChange={F('address')} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-ghost" onClick={() => setEditModal(null)}>Huỷ</button>
                <button type="submit" className="btn btn-primary">Lưu lại</button>
              </div>
            </form>
          </div>
        </div>
      )}
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
