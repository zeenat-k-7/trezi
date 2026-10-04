import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../AuthContext';
import api from '../api';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await api.post('/api/auth/login', { email, password });
      login(res.data.token, res.data.userId, res.data.email);
      navigate('/');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#1a1a2e' }}>
      <div style={{ background: 'white', borderRadius: 12, padding: 40, width: 400, boxShadow: '0 4px 20px rgba(0,0,0,0.3)' }}>
        <h1 style={{ fontSize: 28, fontWeight: 'bold', color: '#00d4aa', textAlign: 'center', marginBottom: 8, margin: 0 }}>TREZI</h1>
        <p style={{ textAlign: 'center', color: '#666', marginBottom: 30, marginTop: 4 }}>Financial Literacy Platform</p>
        {error && <div style={{ background: '#fee', color: '#c33', padding: 10, borderRadius: 6, marginBottom: 16, fontSize: 14 }}>{error}</div>}
        <form onSubmit={handleSubmit}>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14, fontWeight: 500 }}>Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required style={{ width: '100%', padding: 10, border: '1px solid #ddd', borderRadius: 6, marginBottom: 16, boxSizing: 'border-box' }} />
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14, fontWeight: 500 }}>Password</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required style={{ width: '100%', padding: 10, border: '1px solid #ddd', borderRadius: 6, marginBottom: 20, boxSizing: 'border-box' }} />
          <button type="submit" disabled={loading} style={{ width: '100%', padding: 12, background: '#00d4aa', color: 'white', border: 'none', borderRadius: 6, fontSize: 16, fontWeight: 'bold', cursor: 'pointer' }}>
            {loading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
        <p style={{ textAlign: 'center', marginTop: 20, fontSize: 14, color: '#666' }}>
          Don't have an account? <Link to="/register" style={{ color: '#00d4aa' }}>Register</Link>
        </p>
      </div>
    </div>
  );
}
