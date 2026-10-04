import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api';

export default function RegisterPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');
    try {
      await api.post('/api/auth/register', { email, password, phoneNumber });
      setSuccess('Registration successful! Please login.');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh', background: '#1a1a2e' }}>
      <div style={{ background: 'white', borderRadius: 12, padding: 40, width: 400, boxShadow: '0 4px 20px rgba(0,0,0,0.3)' }}>
        <h1 style={{ fontSize: 28, fontWeight: 'bold', color: '#00d4aa', textAlign: 'center', marginBottom: 8, margin: 0 }}>TREZI</h1>
        <p style={{ textAlign: 'center', color: '#666', marginBottom: 30, marginTop: 4 }}>Create an Account</p>
        {error && <div style={{ background: '#fee', color: '#c33', padding: 10, borderRadius: 6, marginBottom: 16, fontSize: 14 }}>{error}</div>}
        {success && <div style={{ background: '#efe', color: '#393', padding: 10, borderRadius: 6, marginBottom: 16, fontSize: 14 }}>{success}</div>}
        <form onSubmit={handleSubmit}>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14, fontWeight: 500 }}>Email</label>
          <input type="email" value={email} onChange={e => setEmail(e.target.value)} required style={{ width: '100%', padding: 10, border: '1px solid #ddd', borderRadius: 6, marginBottom: 16, boxSizing: 'border-box' }} />
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14, fontWeight: 500 }}>Password</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required style={{ width: '100%', padding: 10, border: '1px solid #ddd', borderRadius: 6, marginBottom: 16, boxSizing: 'border-box' }} />
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14, fontWeight: 500 }}>Phone Number (Optional)</label>
          <input type="text" value={phoneNumber} onChange={e => setPhoneNumber(e.target.value)} style={{ width: '100%', padding: 10, border: '1px solid #ddd', borderRadius: 6, marginBottom: 20, boxSizing: 'border-box' }} />
          <button type="submit" disabled={loading} style={{ width: '100%', padding: 12, background: '#00d4aa', color: 'white', border: 'none', borderRadius: 6, fontSize: 16, fontWeight: 'bold', cursor: 'pointer' }}>
            {loading ? 'Registering...' : 'Register'}
          </button>
        </form>
        <p style={{ textAlign: 'center', marginTop: 20, fontSize: 14, color: '#666' }}>
          Already have an account? <Link to="/login" style={{ color: '#00d4aa' }}>Sign In</Link>
        </p>
      </div>
    </div>
  );
}
