import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getDashboard, getLowStock } from '../api';

const fmt = (n) => n != null ? Number(n).toLocaleString('vi-VN') + ' ₫' : '—';

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [lowStock, setLowStock] = useState([]);

  useEffect(() => {
    getDashboard().then(r => setStats(r.data)).catch(() => {});
    getLowStock().then(r => setLowStock(r.data)).catch(() => {});
  }, []);

  if (!stats) return <div className="spinner-wrap"><div className="spinner" /></div>;

  return (
    <>
      {/* Stats */}
      <div className="stat-grid">
        {[
          { label: 'Khách hàng',     value: stats.customerCount,    link: '/customers',              icon: '👤' },
          { label: 'Tổng đơn hàng',   value: stats.orderCount,       link: '/orders',                 icon: '📋' },
          { label: 'Đang thực hiện',   value: stats.activeOrderCount, link: '/orders?status=Dang+may', icon: '🧵' },
          { label: 'Vải sắp hết',      value: stats.lowStockCount,    link: '/materials',              icon: '⚠️' },
        ].map(s => (
          <div className="stat-card" key={s.label}>
            <div className="stat-label">{s.icon} {s.label}</div>
            <div className="stat-value">{s.value}</div>
            <Link className="stat-link" to={s.link}>Xem chi tiết →</Link>
          </div>
        ))}
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 20 }}>
        {/* Quick Actions */}
        <div className="card card-pad">
          <div className="card-title">Lối tắt</div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
            <Link to="/orders/new" className="btn btn-primary" style={{ justifyContent: 'flex-start', gap: 12 }}>
              <span>+</span> Tạo đơn hàng mới
            </Link>
            <Link to="/customers" className="btn btn-ghost" style={{ justifyContent: 'flex-start', gap: 12 }}>
              <span>+</span> Thêm khách hàng
            </Link>
            <Link to="/materials" className="btn btn-ghost" style={{ justifyContent: 'flex-start', gap: 12 }}>
              <span>+</span> Nhập kho vải
            </Link>
            <Link to="/product-types" className="btn btn-ghost" style={{ justifyContent: 'flex-start', gap: 12 }}>
              <span>⚙</span> Cài đặt sản phẩm
            </Link>
          </div>
        </div>

        {/* Low Stock */}
        {lowStock.length > 0 && (
          <div className="card">
            <div className="card-pad" style={{ paddingBottom: 0 }}>
              <div className="card-title">⚠️ Cảnh báo kho vải</div>
            </div>
            <div className="tbl-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Loại vải</th>
                    <th>Màu</th>
                    <th style={{ textAlign: 'right' }}>Tồn kho</th>
                  </tr>
                </thead>
                <tbody>
                  {lowStock.map(m => (
                    <tr key={m.id}>
                      <td><strong>{m.name}</strong></td>
                      <td style={{ color: 'var(--text-muted)' }}>{m.color}</td>
                      <td style={{ textAlign: 'right' }}>
                        <span className="status-pill s-cancel">{m.quantity} {m.unit || 'm'}</span>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
      </div>
    </>
  );
}
