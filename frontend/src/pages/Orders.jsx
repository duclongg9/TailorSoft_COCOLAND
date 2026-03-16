import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { getOrders } from '../api';

const fmt  = (n) => n != null ? Number(n).toLocaleString('vi-VN') + ' ₫' : '—';
const fmtD = (d) => d ? new Date(d).toLocaleDateString('vi-VN') : '—';

const ST = {
  'Dang may':   { label: 'Đang may',   cls: 's-active',  icon: '🧵', dot: '#B8972E' },
  'Hoan thanh': { label: 'Hoàn thành', cls: 's-done',    icon: '✅', dot: '#2D6A4F' },
  'Don huy':    { label: 'Đã hủy',     cls: 's-cancel',  icon: '🚫', dot: '#8B2121' },
};

const FILTERS = [
  ['', 'Tất cả'],
  ['Dang may', '🧵 Đang may'],
  ['Hoan thanh', '✅ Hoàn thành'],
  ['Chua thanh toan', '💰 Chưa TT'],
  ['Don huy', '🚫 Đã hủy'],
];

const PAGE_SIZE = 9; // 3×3 grid

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

export default function Orders() {
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');
  const [search, setSearch]     = useState('');
  const [dateFrom, setDateFrom] = useState('');
  const [dateTo, setDateTo]     = useState('');
  const [page, setPage]         = useState(1);
  const [toast, setToast]       = useState('');

  const load = () => {
    setLoading(true);
    getOrders().then(r => { 
      setOrders(r.data); 
      setLoading(false); 
    }).catch(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const getDisplayStatus = (o) => {
    if (o.status === 'Hoan thanh' && (o.total || 0) - (o.deposit || 0) > 0) {
      return { label: 'Chưa TT', cls: 's-cancel', icon: '💰', dot: '#8B2121' };
    }
    return ST[o.status] ?? { label: o.status, cls: '', icon: '?', dot: '#888' };
  };

  const filtered = orders.filter(o => {
    // 1. Status filter
    if (filter === 'Dang may' && o.status !== 'Dang may') return false;
    if (filter === 'Hoan thanh' && o.status !== 'Hoan thanh') return false;
    if (filter === 'Don huy' && o.status !== 'Don huy') return false;
    if (filter === 'Chua thanh toan') {
      const isPending = o.status === 'Hoan thanh' && (o.total || 0) - (o.deposit || 0) > 0;
      if (!isPending) return false;
    }

    // 2. Search
    const matchSearch = !search || 
      (o.customerName || '').toLowerCase().includes(search.toLowerCase()) || 
      String(o.id).includes(search);
    if (!matchSearch) return false;

    // 3. Date range
    if (dateFrom && o.orderDate < dateFrom) return false;
    if (dateTo && o.orderDate > dateTo) return false;

    return true;
  });

  const paged = filtered.slice((page - 1) * PAGE_SIZE, page * PAGE_SIZE);

  return (
    <>
      <div className="page-hdr">
        <h2>Đơn hàng</h2>
        <button className="btn btn-primary btn-top" onClick={() => navigate('/orders/new')}>+ Tạo đơn mới</button>
      </div>

      {/* Status Filter */}
      <div className="hide-scrollbar" style={{ display: 'flex', gap: 8, marginBottom: 16, overflowX: 'auto', paddingBottom: 4 }}>
        {FILTERS.map(([v, l]) => (
          <button key={v}
            className={`btn btn-sm ${filter === v ? 'btn-primary' : 'btn-ghost'}`}
            style={{ whiteSpace: 'nowrap' }}
            onClick={() => { setFilter(v); setPage(1); }}>{l}</button>
        ))}
      </div>

      {/* Search & Date Filter */}
      <div className="card" style={{ marginBottom: 24, padding: '16px' }}>
        <div style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
          <input className="form-input" placeholder="Tìm tên khách hoặc mã đơn…"
            value={search} onChange={e => { setSearch(e.target.value); setPage(1); }} />
          
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10 }}>
            <div style={{ border: '1px solid var(--border)', borderRadius: 8, padding: '4px 10px' }}>
              <label className="stat-label" style={{ fontSize: 9 }}>Từ ngày</label>
              <input type="date" className="form-input" style={{ height: 32, border: 'none', padding: 0, fontSize: 13 }} 
                value={dateFrom} onChange={e => { setDateFrom(e.target.value); setPage(1); }} />
            </div>
            <div style={{ border: '1px solid var(--border)', borderRadius: 8, padding: '4px 10px' }}>
              <label className="stat-label" style={{ fontSize: 9 }}>Đến ngày</label>
              <input type="date" className="form-input" style={{ height: 32, border: 'none', padding: 0, fontSize: 13 }} 
                value={dateTo} onChange={e => { setDateTo(e.target.value); setPage(1); }} />
            </div>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="spinner-wrap"><div className="spinner" /></div>
      ) : filtered.length === 0 ? (
        <div className="empty-state"><div className="icon">📋</div>Không tìm thấy đơn hàng nào</div>
      ) : (
        <>
          <div className="order-grid">
            {paged.map(o => {
              const s = getDisplayStatus(o);
              const balance = (o.total || 0) - (o.deposit || 0);
              return (
                <div key={o.id} className="order-card" onClick={() => navigate(`/orders/${o.id}`)}>
                  <div style={{ height: 5, background: s.dot, borderRadius: '14px 14px 0 0' }} />
                  <div className="card-body-internal" style={{ padding: '18px 20px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
                      <div style={{ fontFamily: 'var(--serif)', fontSize: 22, fontWeight: 700, color: 'var(--accent)' }}>#{o.id}</div>
                      <span className={`status-pill ${s.cls}`} style={{ fontSize: 12 }}>{s.icon} {s.label}</span>
                    </div>
                    <div style={{ fontWeight: 700, fontSize: 17, marginBottom: 2 }}>{o.customerName || '—'}</div>
                    <div style={{ color: 'var(--text-muted)', fontSize: 14, marginBottom: 14 }}>{o.customerPhone}</div>
                    <div className="order-card-dates">
                      <div style={{ textAlign: 'center', flex: 1 }}>
                        <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--text-muted)', marginBottom: 3 }}>Đặt hàng</div>
                        <div style={{ fontSize: 14, fontWeight: 600 }}>{fmtD(o.orderDate)}</div>
                      </div>
                      <div className="arrow-icon" style={{ color: 'var(--accent)', fontSize: 20, margin: '0 10px' }}>→</div>
                      <div style={{ textAlign: 'center', flex: 1 }}>
                        <div style={{ fontSize: 11, fontWeight: 700, letterSpacing: '0.08em', textTransform: 'uppercase', color: 'var(--text-muted)', marginBottom: 3 }}>Giao hàng</div>
                        <div style={{ fontSize: 14, fontWeight: 600 }}>{fmtD(o.deliveryDate)}</div>
                      </div>
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                      <div>
                        <div style={{ fontSize: 11, fontWeight: 700, textTransform: 'uppercase', color: 'var(--text-muted)', letterSpacing: '0.08em' }}>Tổng tiền</div>
                        <div style={{ fontFamily: 'var(--serif)', fontSize: 20, fontWeight: 700 }}>{fmt(o.total)}</div>
                      </div>
                      {balance > 0 && (
                        <div style={{ textAlign: 'right' }}>
                          <div style={{ fontSize: 11, fontWeight: 700, textTransform: 'uppercase', color: 'var(--danger)', letterSpacing: '0.08em' }}>Còn lại</div>
                          <div style={{ fontFamily: 'var(--serif)', fontSize: 17, fontWeight: 700, color: 'var(--danger)' }}>{fmt(balance)}</div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
          <Pagination page={page} total={filtered.length} perPage={PAGE_SIZE} onChange={setPage} />
        </>
      )}

      {/* Floating Button on Mobile */}
      <button className="btn-fab" onClick={() => navigate('/orders/new')}>+</button>
      
      {toast && <div className="toast">{toast}</div>}
    </>
  );
}
