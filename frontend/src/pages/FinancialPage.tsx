import { useState, useEffect } from 'react';
import api from '../api';

interface SnapshotFields {
  monthlyIncome: string;
  monthlyExpenses: string;
  totalSavings: string;
  totalDebt: string;
  totalInvestments: string;
  netWorth: string;
}

const fieldLabels: Record<keyof SnapshotFields, string> = {
  monthlyIncome: 'Monthly Income (₹)',
  monthlyExpenses: 'Monthly Expenses (₹)',
  totalSavings: 'Total Savings (₹)',
  totalDebt: 'Total Debt (₹)',
  totalInvestments: 'Total Investments (₹)',
  netWorth: 'Net Worth (₹)',
};

export default function FinancialPage() {
  const [financials, setFinancials] = useState<SnapshotFields>({
    monthlyIncome: '', monthlyExpenses: '', totalSavings: '',
    totalDebt: '', totalInvestments: '', netWorth: ''
  });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    api.get('/api/financial/snapshot/latest')
      .then(res => {
        if (res.data) {
          setFinancials({
            monthlyIncome: res.data.monthlyIncome ?? '',
            monthlyExpenses: res.data.monthlyExpenses ?? '',
            totalSavings: res.data.totalSavings ?? '',
            totalDebt: res.data.totalDebt ?? '',
            totalInvestments: res.data.totalInvestments ?? '',
            netWorth: res.data.netWorth ?? '',
          });
        }
      })
      .catch(err => {
        if (err.response?.status !== 404) console.error(err);
      })
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setMessage('');
    setIsError(false);
    const today = new Date().toISOString().split('T')[0]; // YYYY-MM-DD
    try {
      await api.post('/api/financial/snapshot', {
        ...financials,
        monthlyIncome: parseFloat(financials.monthlyIncome) || 0,
        monthlyExpenses: parseFloat(financials.monthlyExpenses) || 0,
        totalSavings: parseFloat(financials.totalSavings) || 0,
        totalDebt: parseFloat(financials.totalDebt) || 0,
        totalInvestments: parseFloat(financials.totalInvestments) || 0,
        netWorth: parseFloat(financials.netWorth) || 0,
        snapshotDate: today,
      });
      setMessage('✓ Financial snapshot saved successfully!');
    } catch (err: any) {
      setIsError(true);
      const msg = err.response?.data?.error || err.response?.data?.message || 'Error saving snapshot.';
      setMessage(msg);
    } finally {
      setSaving(false);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFinancials({ ...financials, [e.target.name]: e.target.value });
  };

  if (loading) return <div style={{ padding: 40, textAlign: 'center', color: '#888' }}>Loading…</div>;

  return (
    <div style={{ background: 'white', padding: 32, borderRadius: 10, boxShadow: '0 2px 8px rgba(0,0,0,0.07)', maxWidth: 560 }}>
      <h2 style={{ margin: '0 0 8px 0', color: '#1a1a2e', fontSize: 22 }}>Financial Snapshot</h2>
      <p style={{ color: '#888', marginBottom: 24, fontSize: 14 }}>
        Your financial data helps TREZI give you personalised advice. All values in Rupees.
      </p>

      {message && (
        <div style={{
          marginBottom: 16, padding: '10px 14px', borderRadius: 6, fontSize: 14,
          background: isError ? '#fee' : '#efffef',
          color: isError ? '#c33' : '#2a7a2a',
          border: `1px solid ${isError ? '#fcc' : '#b2e0b2'}`,
        }}>
          {message}
        </div>
      )}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        {(Object.keys(fieldLabels) as (keyof SnapshotFields)[]).map(field => (
          <div key={field}>
            <label style={{ display: 'block', marginBottom: 5, fontSize: 14, fontWeight: 500, color: '#444' }}>
              {fieldLabels[field]}
            </label>
            <input
              type="number"
              name={field}
              value={financials[field]}
              onChange={handleChange}
              min="0"
              step="0.01"
              placeholder="0"
              required
              style={{
                width: '100%', padding: '9px 12px', border: '1px solid #ddd', borderRadius: 6,
                fontSize: 15, boxSizing: 'border-box', outline: 'none',
              }}
            />
          </div>
        ))}
        <button
          type="submit"
          disabled={saving}
          style={{
            padding: '12px 0', background: saving ? '#aaa' : '#00d4aa', color: 'white',
            border: 'none', borderRadius: 6, cursor: saving ? 'default' : 'pointer',
            fontWeight: 700, fontSize: 15, marginTop: 6,
          }}
        >
          {saving ? 'Saving…' : 'Save Snapshot'}
        </button>
      </form>
    </div>
  );
}
