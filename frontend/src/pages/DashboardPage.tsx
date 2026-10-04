import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

interface DashboardData {
  email: string;
  hasProfile: boolean;
  hasFinancialData: boolean;
  hasAssessment: boolean;
  literacyLevel: string | null;
  overallScore: number | null;
  monthlyIncome: number | null;
  monthlyExpenses: number | null;
  totalSavings: number | null;
  netWorth: number | null;
  conversationCount: number;
  recentConversations: any[];
}

const fmt = (v: number | null) =>
  v != null ? `₹${Number(v).toLocaleString('en-IN')}` : '—';

export default function DashboardPage() {
  const [data, setData] = useState<DashboardData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/dashboard/')
      .then(res => setData(res.data))
      .catch(() => setError('Could not load dashboard. Please try again.'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div style={{ padding: 40, textAlign: 'center', color: '#888' }}>Loading dashboard…</div>;
  if (error) return <div style={{ padding: 40, color: '#c33' }}>{error}</div>;
  if (!data) return null;

  const card = {
    background: 'white', padding: 24, borderRadius: 10,
    boxShadow: '0 2px 8px rgba(0,0,0,0.07)',
  };

  const pill = (label: string, color: string) => (
    <span style={{ background: color, color: 'white', borderRadius: 12, padding: '3px 12px', fontSize: 13, fontWeight: 600 }}>
      {label}
    </span>
  );

  return (
    <div>
      <h2 style={{ fontSize: 26, margin: '0 0 6px 0', color: '#1a1a2e' }}>
        Welcome back, {data.email?.split('@')[0]} 👋
      </h2>
      <p style={{ color: '#888', marginBottom: 28, fontSize: 14 }}>
        Here's a summary of your financial literacy journey.
      </p>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: 20, marginBottom: 24 }}>

        {/* Profile card */}
        <div style={card}>
          <h3 style={{ margin: '0 0 12px 0', fontSize: 16, color: '#555' }}>👤 Profile</h3>
          {data.hasProfile
            ? <p style={{ color: '#00d4aa', fontWeight: 600 }}>✓ Complete</p>
            : <>
                <p style={{ color: '#e6a23c', marginBottom: 12 }}>⚠ Not set up yet</p>
                <Link to="/profile" style={{ color: '#00d4aa', textDecoration: 'none', fontWeight: 600 }}>
                  Set up profile →
                </Link>
              </>
          }
        </div>

        {/* Financial Snapshot card */}
        <div style={card}>
          <h3 style={{ margin: '0 0 12px 0', fontSize: 16, color: '#555' }}>💰 Financial Snapshot</h3>
          {data.hasFinancialData ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 6 }}>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: '#888', fontSize: 14 }}>Monthly Income</span>
                <strong>{fmt(data.monthlyIncome)}</strong>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: '#888', fontSize: 14 }}>Monthly Expenses</span>
                <strong>{fmt(data.monthlyExpenses)}</strong>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', borderTop: '1px solid #eee', paddingTop: 6, marginTop: 4 }}>
                <span style={{ color: '#888', fontSize: 14 }}>Net Worth</span>
                <strong style={{ color: '#00d4aa' }}>{fmt(data.netWorth)}</strong>
              </div>
            </div>
          ) : (
            <>
              <p style={{ color: '#888', marginBottom: 12, fontSize: 14 }}>No financial data yet.</p>
              <Link to="/financial" style={{ color: '#00d4aa', textDecoration: 'none', fontWeight: 600 }}>
                Add snapshot →
              </Link>
            </>
          )}
        </div>

        {/* Assessment card */}
        <div style={card}>
          <h3 style={{ margin: '0 0 12px 0', fontSize: 16, color: '#555' }}>📝 Literacy Assessment</h3>
          {data.hasAssessment ? (
            <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
              <div style={{ fontSize: 36, fontWeight: 800, color: '#00d4aa' }}>
                {data.overallScore}%
              </div>
              <div>
                {pill(data.literacyLevel ?? 'BEGINNER',
                  data.literacyLevel === 'ADVANCED' ? '#00d4aa' :
                  data.literacyLevel === 'INTERMEDIATE' ? '#e6a23c' : '#e06c75')}
              </div>
              <Link to="/assessment" style={{ color: '#888', textDecoration: 'none', fontSize: 13, marginTop: 4 }}>
                Retake assessment →
              </Link>
            </div>
          ) : (
            <>
              <p style={{ color: '#888', marginBottom: 12, fontSize: 14 }}>Not taken yet.</p>
              <Link to="/assessment" style={{ color: '#00d4aa', textDecoration: 'none', fontWeight: 600 }}>
                Take assessment →
              </Link>
            </>
          )}
        </div>

        {/* AI Conversations card */}
        <div style={card}>
          <h3 style={{ margin: '0 0 12px 0', fontSize: 16, color: '#555' }}>🤖 AI Consultations</h3>
          <div style={{ fontSize: 32, fontWeight: 800, color: '#1a1a2e', marginBottom: 8 }}>
            {data.conversationCount}
          </div>
          <p style={{ color: '#888', fontSize: 14, marginBottom: 12 }}>
            {data.conversationCount === 0 ? 'No conversations yet.' : 'total conversations'}
          </p>
          <Link to="/chat" style={{
            display: 'inline-block', background: '#00d4aa', color: 'white',
            padding: '8px 16px', borderRadius: 6, textDecoration: 'none', fontWeight: 600, fontSize: 14
          }}>
            💬 Ask TREZI
          </Link>
        </div>

      </div>

      {/* Recent conversations */}
      {data.recentConversations?.length > 0 && (
        <div style={card}>
          <h3 style={{ margin: '0 0 12px 0', fontSize: 16, color: '#555' }}>Recent Conversations</h3>
          {data.recentConversations.map((c: any) => (
            <Link key={c.id} to={`/chat/${c.id}`} style={{
              display: 'block', padding: '10px 0', borderBottom: '1px solid #f0f0f0',
              textDecoration: 'none', color: '#333', fontSize: 14,
            }}>
              💬 {c.title || 'Conversation'} <span style={{ color: '#bbb', fontSize: 12, float: 'right' }}>
                {c.updatedAt ? new Date(c.updatedAt).toLocaleDateString() : ''}
              </span>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
