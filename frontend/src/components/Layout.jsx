import { useState } from 'react';
import { Outlet, NavLink, useLocation } from 'react-router-dom';

const NAV = [
  { to: '/',              icon: '◈', label: 'Dashboard'  },
  { to: '/customers',     icon: '◉', label: 'Khách hàng'  },
  { to: '/orders',        icon: '◈', label: 'Đơn hàng'   },
  { to: '/materials',     icon: '◎', label: 'Kho vải'    },
  { to: '/product-types', icon: '◇', label: 'Sản phẩm'  },
];

const PAGE_TITLES = {
  '/': 'Dashboard',
  '/customers': 'Khách hàng',
  '/orders': 'Đơn hàng',
  '/orders/new': 'Tạo đơn hàng',
  '/materials': 'Kho vải',
  '/product-types': 'Thiết lập sản phẩm',
};

export default function Layout() {
  const [open, setOpen] = useState(false);
  const { pathname } = useLocation();
  const title = PAGE_TITLES[pathname] ?? 'COCOLAND';
  const close = () => setOpen(false);

  return (
    <div className="app-layout">
      {open && <div className={`sidebar-overlay ${open ? 'open' : ''}`} onClick={close} />}

      <aside className={`sidebar ${open ? 'open' : ''}`}>
        <div className="sidebar-logo">
          COCOLAND
          <small>Atelier Management</small>
        </div>
        <nav>
          {NAV.map(n => (
            <NavLink key={n.to} to={n.to} end={n.to === '/'}
              className={({ isActive }) => isActive ? 'active' : ''}
              onClick={close}>
              <span className="icon">{n.icon}</span>
              {n.label}
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-footer">© 2025 Cocoland</div>
      </aside>

      <div className="main-content">
        <header className="topbar">
          <button className="menu-btn" onClick={() => setOpen(v => !v)}>☰</button>
          <h1>{title}</h1>
        </header>
        <div className="page-content">
          <Outlet />
        </div>
      </div>

      <nav className="bottom-nav">
        {NAV.map(n => (
          <NavLink key={n.to} to={n.to} end={n.to === '/'}
            className={({ isActive }) => isActive ? 'active' : ''}
            onClick={close}>
            <span className="icon">{n.icon}</span>
            {n.label}
          </NavLink>
        ))}
      </nav>
    </div>
  );
}
