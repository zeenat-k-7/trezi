import { Outlet, Link, useLocation } from 'react-router-dom';
import { useAuth } from '../AuthContext';

export default function Layout() {
  const { email, logout } = useAuth();
  const location = useLocation();

  const navItems = [
    { path: '/', label: '📊 Dashboard', icon: '📊' },
    { path: '/profile', label: '👤 Profile', icon: '👤' },
    { path: '/financial', label: '💰 Financial', icon: '💰' },
    { path: '/assessment', label: '📝 Assessment', icon: '📝' },
    { path: '/chat', label: '🤖 AI Assistant', icon: '🤖' },
  ];

  return (
    <div style={{ display: 'flex', minHeight: '100vh', fontFamily: 'system-ui, -apple-system, sans-serif' }}>
      <nav style={{
        width: 240, background: '#1a1a2e', color: 'white', padding: '20px 0',
        display: 'flex', flexDirection: 'column'
      }}>
        <div style={{ padding: '0 20px', marginBottom: 30 }}>
          <h1 style={{ fontSize: 24, fontWeight: 'bold', color: '#00d4aa', margin: 0 }}>TREZI</h1>
          <p style={{ fontSize: 12, color: '#888', marginTop: 4 }}>Financial Literacy AI</p>
        </div>
        {navItems.map(item => (
          <Link key={item.path} to={item.path} style={{
            display: 'block', padding: '12px 20px', textDecoration: 'none',
            color: location.pathname === item.path || (location.pathname.startsWith('/chat') && item.path === '/chat') ? '#00d4aa' : '#ccc',
            background: location.pathname === item.path || (location.pathname.startsWith('/chat') && item.path === '/chat') ? '#16213e' : 'transparent',
            borderLeft: location.pathname === item.path || (location.pathname.startsWith('/chat') && item.path === '/chat') ? '3px solid #00d4aa' : '3px solid transparent',
            fontSize: 14,
          }}>{item.label}</Link>
        ))}
        <div style={{ marginTop: 'auto', padding: '20px', borderTop: '1px solid #333' }}>
          <p style={{ fontSize: 12, color: '#888', marginBottom: 8, wordBreak: 'break-all' }}>{email}</p>
          <button onClick={logout} style={{
            background: 'none', border: '1px solid #555', color: '#ccc',
            padding: '6px 12px', borderRadius: 4, cursor: 'pointer', fontSize: 12, width: '100%'
          }}>Logout</button>
        </div>
      </nav>
      <main style={{ flex: 1, background: '#f5f7fa', padding: 30, overflowY: 'auto' }}>
        <Outlet />
      </main>
    </div>
  );
}
