import { useEffect, useState } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { imgUrl, updateDeliveryDate, updateAmount, updateStatus, getMeasurements, getOrderDetails, getOrder, uploadDepositImage, uploadFullImage, sendEmail } from '../api';
import CurrencyInput from '../components/CurrencyInput';

const fmt  = (n) => n != null ? Number(n).toLocaleString('vi-VN') + ' ₫' : '—';
const fmtD = (d) => d ? new Date(d).toLocaleDateString('vi-VN') : '—';
const toISO = (d) => d ? (typeof d === 'string' ? d.slice(0, 10) : new Date(d).toISOString().slice(0, 10)) : '';

const ST = {
  'Dang may':   { label: 'Đang thực hiện', cls: 's-active', icon: '🧵' },
  'Hoan thanh': { label: 'Đã hoàn thành',  cls: 's-done',   icon: '✅' },
  'Don huy':    { label: 'Đã hủy bỏ',      cls: 's-cancel', icon: '🚫' },
};

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

const DETAIL_PAGE = 3;

export default function OrderDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [order,   setOrder]   = useState(null);
  const [details, setDetails] = useState([]);
  const [toast,   setToast]   = useState('');
  const [editAmt, setEditAmt] = useState(false);
  const [editDate, setEditDate] = useState(false);
  const [amount,  setAmount]  = useState({ total: 0, deposit: 0 });
  const [newDelivery, setNewDelivery] = useState('');
  const [viewer,  setViewer]  = useState(null);
  const [mModal,  setMModal]  = useState(null);
  const [detailPage, setDetailPage] = useState(1);

  const load = () => {
    getOrder(id).then(r => {
      setOrder(r.data);
      setAmount({ total: r.data.total || 0, deposit: r.data.deposit || 0 });
      setNewDelivery(toISO(r.data.deliveryDate));
    }).catch(() => showToast('Không tìm được đơn hàng'));
    getOrderDetails(id).then(r => setDetails(r.data));
  };
  useEffect(() => { load(); }, [id]);

  const showToast = (m) => { setToast(m); setTimeout(() => setToast(''), 2500); };

  const changeStatus = async (s) => {
    if (!confirm(`Xác nhận: chuyển sang "${ST[s]?.label}"?`)) return;
    await updateStatus(id, s);
    showToast('Đã cập nhật'); load();
  };

  const saveAmount = async () => {
    if (!confirm('Xác nhận cập nhật thanh toán?')) return;
    await updateAmount(id, { total: Number(amount.total), deposit: Number(amount.deposit) });
    setEditAmt(false); showToast('Đã lưu'); load();
  };

  const saveDelivery = async () => {
    if (!newDelivery) { showToast('Vui lòng chọn ngày'); return; }
    if (order.orderDate && newDelivery < toISO(order.orderDate)) {
      showToast('Ngày giao phải sau ngày đặt!'); return;
    }
    if (!confirm('Xác nhận cập nhật ngày giao?')) return;
    await updateDeliveryDate(id, newDelivery);
    setEditDate(false); showToast('Đã cập nhật ngày giao'); load();
  };

  const onUpload = async (type, file) => {
    if (!file) return;
    if (!confirm('Tải ảnh chứng từ lên?')) return;
    try {
      type === 'deposit' ? await uploadDepositImage(id, file) : await uploadFullImage(id, file);
      showToast('Tải ảnh thành công'); load();
    } catch { showToast('Lỗi khi tải ảnh'); }
  };

  const viewMeasure = async (d) => {
    const r = await getMeasurements({ orderDetailId: d.id }).catch(() => ({ data: [] }));
    setMModal({ item: d, list: r.data });
  };

  if (!order) return <div className="spinner-wrap"><div className="spinner" /></div>;

  const s = ST[order.status] ?? { label: order.status, cls: '', icon: '?' };
  const balance = (order.total || 0) - (order.deposit || 0);
  const pagedDetails = details.slice((detailPage - 1) * DETAIL_PAGE, detailPage * DETAIL_PAGE);

  return (
    <>
      {/* Image Viewer */}
      {viewer && (
        <div className="img-viewer" onClick={() => setViewer(null)}>
          <div className="img-viewer-close" onClick={() => setViewer(null)}>✕</div>
          <img src={viewer} alt="Xem ảnh" onClick={e => e.stopPropagation()} />
        </div>
      )}

      {/* Header */}
      <div style={{ display: 'flex', alignItems: 'center', gap: 16, marginBottom: 28, flexWrap: 'wrap' }}>
        <Link to="/orders" className="btn btn-ghost btn-sm" style={{ padding: '0 12px' }}>←</Link>
        <h2 style={{ flex: 1, fontFamily: 'var(--serif)', fontSize: 'clamp(24px, 5vw, 30px)', fontWeight: 700, margin: 0 }}>
          Đơn <span style={{ color: 'var(--accent)' }}>#{order.id}</span>
        </h2>
        <span className={`status-pill ${s.cls}`} style={{ fontSize: 13, padding: '8px 16px' }}>
          {s.icon} {s.label}
        </span>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 20, marginBottom: 20 }}>
        {/* Customer */}
        <div className="card card-pad">
          <div className="card-title">Khách hàng</div>
          <div style={{ fontFamily: 'var(--serif)', fontSize: 24, fontWeight: 700, marginBottom: 8 }}>{order.customerName}</div>
          <div style={{ color: 'var(--accent)', fontWeight: 600, fontSize: 18 }}>{order.customerPhone}</div>
          <div style={{ color: 'var(--text-muted)', fontSize: 14, marginTop: 2 }}>{order.customerEmail}</div>
          {order.customerAddress && (
            <div style={{ marginTop: 14, padding: 12, background: 'var(--bg-warm)', borderRadius: 8, fontSize: 14 }}>
              🏠 {order.customerAddress}
            </div>
          )}

          {/* Dates section */}
          <div className="grid-2-1" style={{ marginTop: 16, gap: 10 }}>
            <div style={{ padding: 12, background: 'var(--bg)', border: '1px solid var(--border)', borderRadius: 8 }}>
              <div className="stat-label">Ngày đặt</div>
              <div style={{ fontWeight: 700, fontSize: 15 }}>{fmtD(order.orderDate)}</div>
            </div>
            <div style={{ padding: 12, background: editDate ? 'var(--accent-light)' : 'var(--bg)', border: `1px solid ${editDate ? 'var(--accent)' : 'var(--border)'}`, borderRadius: 8 }}>
              {!editDate ? (
                <>
                  <div className="stat-label">Ngày giao</div>
                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <div style={{ fontWeight: 700, fontSize: 15 }}>{fmtD(order.deliveryDate)}</div>
                    <button className="btn btn-ghost btn-xs" onClick={() => setEditDate(true)}
                      title="Đổi ngày giao">✎</button>
                  </div>
                </>
              ) : (
                <>
                  <label className="form-label">Ngày giao mới</label>
                  <input type="date" className="form-input" style={{ height: 40, marginBottom: 8 }}
                    value={newDelivery} min={toISO(order.orderDate)}
                    onChange={e => setNewDelivery(e.target.value)} />
                  <div style={{ display: 'flex', gap: 6 }}>
                    <button className="btn btn-primary btn-xs" style={{ flex: 1 }} onClick={saveDelivery}>Lưu</button>
                    <button className="btn btn-ghost btn-xs" onClick={() => setEditDate(false)}>✕</button>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>

        {/* Payment */}
        <div className="card card-pad">
          <div className="card-title">Thanh toán</div>
          {!editAmt ? (
            <>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ color: 'var(--text-muted)', fontSize: 15 }}>Tổng cộng</span>
                  <span style={{ fontFamily: 'var(--serif)', fontSize: 24, fontWeight: 700 }}>{fmt(order.total)}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ color: 'var(--text-muted)', fontSize: 15 }}>Đã cọc</span>
                  <span style={{ color: 'var(--success)', fontWeight: 700, fontSize: 18 }}>{fmt(order.deposit)}</span>
                </div>
                <hr style={{ border: 'none', borderTop: '1px solid var(--border)' }} />
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span style={{ fontWeight: 700, fontSize: 16 }}>Còn lại</span>
                  <span style={{ fontFamily: 'var(--serif)', fontSize: 30, fontWeight: 700, color: balance > 0 ? 'var(--danger)' : 'var(--success)' }}>
                    {fmt(balance)}
                  </span>
                </div>
              </div>
              <div style={{ marginTop: 20, display: 'flex', gap: 10 }}>
                <button className="btn btn-ghost btn-sm" style={{ flex: 1 }} onClick={() => setEditAmt(true)}>✎ Sửa tiền</button>
                {order.status !== 'Don huy' && order.status !== 'Hoan thanh' && (
                  <button className="btn btn-danger btn-sm" style={{ flex: 1 }} onClick={() => changeStatus('Don huy')}>🚫 Hủy đơn</button>
                )}
              </div>
              {order.status === 'Dang may' && (
                <button className="btn btn-accent" style={{ width: '100%', marginTop: 12, height: 54, fontSize: 16 }}
                  onClick={() => changeStatus('Hoan thanh')}>✅ Hoàn thành đơn</button>
              )}
            </>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
              <CurrencyInput
                label="Tổng tiền đơn hàng"
                value={amount.total}
                onChange={val => setAmount(a => ({ ...a, total: val }))}
              />
              <CurrencyInput
                label="Tiền đã cọc"
                value={amount.deposit}
                onChange={val => setAmount(a => ({ ...a, deposit: val }))}
              />
              <div style={{ display: 'flex', gap: 10 }}>
                <button className="btn btn-primary" style={{ flex: 2 }} onClick={saveAmount}>Lưu thay đổi</button>
                <button className="btn btn-ghost" style={{ flex: 1 }} onClick={() => setEditAmt(false)}>Huỷ</button>
              </div>
            </div>
          )}
        </div>

        {/* Proofs */}
        <div className="card card-pad">
          <div className="card-title">Chứng từ</div>
          <div className="grid-2-1">
            {[
              { key: 'depositImage', label: 'Ảnh cọc', type: 'deposit' },
              { key: 'fullImage',    label: 'Ảnh chuyển đủ', type: 'full' },
            ].map(({ key, label, type }) => (
              <div key={key} style={{ textAlign: 'center' }}>
                <div className="section-label" style={{ textAlign: 'center', marginBottom: 8 }}>{label}</div>
                <div onClick={() => order[key] && setViewer(imgUrl(order[key]))}
                  style={{ height: 140, borderRadius: 10, border: '1px solid var(--border)', background: 'var(--bg-warm)', overflow: 'hidden', cursor: order[key] ? 'zoom-in' : 'default', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 32 }}>
                  {order[key]
                    ? <img src={imgUrl(order[key])} style={{ width: '100%', height: '100%', objectFit: 'cover' }} alt={label} />
                    : '📷'}
                </div>
                <label className="btn btn-ghost btn-xs" style={{ marginTop: 8, width: '100%', cursor: 'pointer' }}>
                  Tải lên
                  <input type="file" hidden accept="image/*" onChange={e => onUpload(type, e.target.files[0])} />
                </label>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Product details with pagination */}
      <div className="card" style={{ marginBottom: 20 }}>
        <div className="card-pad" style={{ paddingBottom: 0 }}>
          <div className="card-title">Chi tiết sản phẩm</div>
        </div>
        {details.length === 0 ? (
          <div className="empty-state" style={{ padding: '30px 0' }}>Chưa có sản phẩm</div>
        ) : (
          <>
            <div className="tbl-wrap">
              <table>
                <thead>
                  <tr><th>Sản phẩm</th><th>Chất liệu</th><th style={{ textAlign: 'center' }}>SL</th><th style={{ textAlign: 'right' }}>Đơn giá</th><th>Thông số</th></tr>
                </thead>
                <tbody>
                  {pagedDetails.map(d => (
                    <tr key={d.id}>
                      <td><strong style={{ fontSize: 16 }}>{d.productType}</strong></td>
                      <td style={{ color: 'var(--text-muted)' }}>{d.materialName || '—'}</td>
                      <td style={{ textAlign: 'center', fontFamily: 'var(--serif)', fontSize: 22, fontWeight: 700 }}>{d.quantity}</td>
                      <td style={{ textAlign: 'right', fontWeight: 600, fontSize: 16 }}>{fmt(d.unitPrice)}</td>
                      <td>
                        <button className="btn btn-ghost btn-xs" onClick={() => viewMeasure(d)}>📏 Xem đo</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
            <div style={{ padding: '0 20px' }}>
              <Pagination page={detailPage} total={details.length} perPage={DETAIL_PAGE} onChange={setDetailPage} />
            </div>
          </>
        )}
      </div>

      <button className="btn btn-primary" style={{ width: '100%', height: 58, fontSize: 15, letterSpacing: '0.10em' }}
        onClick={() => { if (confirm('Gửi email thông báo cho khách hàng?')) sendEmail(id).then(() => showToast('Đã gửi email!')); }}>
        📬 GỬI THÔNG BÁO QUA EMAIL
      </button>

      {/* Measure Modal */}
      {mModal && (
        <div className="modal-overlay" onClick={() => setMModal(null)}>
          <div className="modal" style={{ maxWidth: 520 }} onClick={e => e.stopPropagation()}>
            <span className="modal-drag" />
            <div className="modal-hdr">
              <h3>Số đo: {mModal.item.productType}</h3>
              <button className="modal-close" onClick={() => setMModal(null)}>✕</button>
            </div>
            <div className="modal-body">
              {mModal.list.length === 0 ? (
                <div className="empty-state" style={{ padding: '30px 0' }}><div className="icon">📏</div>Chưa có số đo</div>
              ) : (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(130px, 1fr))', gap: 12 }}>
                  {mModal.list.map(m => (
                    <div key={m.id} style={{ padding: '16px 12px', border: '1px solid var(--border)', borderRadius: 10, textAlign: 'center' }}>
                      <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: '0.10em', textTransform: 'uppercase', color: 'var(--text-muted)', marginBottom: 6 }}>
                        {m.measurementTypeName}
                      </div>
                      <div style={{ fontFamily: 'var(--serif)', fontSize: 30, fontWeight: 700, color: 'var(--primary)' }}>
                        {m.value}<span style={{ fontSize: 15, fontWeight: 400, marginLeft: 3 }}>cm</span>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
            <div className="modal-footer">
              <button className="btn btn-primary" onClick={() => setMModal(null)}>Đóng</button>
            </div>
          </div>
        </div>
      )}

      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
