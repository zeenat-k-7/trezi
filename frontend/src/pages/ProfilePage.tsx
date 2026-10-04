import { useState, useEffect } from 'react';
import api from '../api';

export default function ProfilePage() {
  const [profile, setProfile] = useState<any>({
    age: '', profession: '', employmentType: '', dependentsCount: '',
    primaryFinancialGoal: '', riskPreference: 'MODERATE', investmentExperience: 'NONE',
    preferredLanguage: 'en', preferredExplanationStyle: 'Simple'
  });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    api.get('/api/users/me/profile')
      .then(res => {
        if (res.data) setProfile({ ...profile, ...res.data });
      })
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setMessage('');
    try {
      await api.put('/api/users/me/profile', profile);
      setMessage('Profile updated successfully!');
    } catch (err: any) {
      setMessage('Error updating profile.');
    } finally {
      setSaving(false);
    }
  };

  const handleChange = (e: any) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div style={{ background: 'white', padding: 30, borderRadius: 8, boxShadow: '0 2px 4px rgba(0,0,0,0.05)', maxWidth: 600 }}>
      <h2 style={{ margin: '0 0 20px 0', color: '#1a1a2e' }}>User Profile</h2>
      {message && <div style={{ marginBottom: 16, padding: 10, background: '#eee', borderRadius: 4 }}>{message}</div>}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 16 }}>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Age</label>
          <input type="number" name="age" value={profile.age || ''} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }} />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Profession</label>
          <input type="text" name="profession" value={profile.profession || ''} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }} />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Employment Type</label>
          <input type="text" name="employmentType" value={profile.employmentType || ''} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }} />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Dependents Count</label>
          <input type="number" name="dependentsCount" value={profile.dependentsCount || ''} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }} />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Primary Financial Goal</label>
          <input type="text" name="primaryFinancialGoal" value={profile.primaryFinancialGoal || ''} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }} />
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Risk Preference</label>
          <select name="riskPreference" value={profile.riskPreference} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }}>
            <option value="CONSERVATIVE">Conservative</option>
            <option value="MODERATE">Moderate</option>
            <option value="AGGRESSIVE">Aggressive</option>
          </select>
        </div>
        <div>
          <label style={{ display: 'block', marginBottom: 4, fontSize: 14 }}>Investment Experience</label>
          <select name="investmentExperience" value={profile.investmentExperience} onChange={handleChange} style={{ width: '100%', padding: 8, border: '1px solid #ccc', borderRadius: 4 }}>
            <option value="NONE">None</option>
            <option value="BEGINNER">Beginner</option>
            <option value="INTERMEDIATE">Intermediate</option>
            <option value="EXPERIENCED">Experienced</option>
          </select>
        </div>
        <button type="submit" disabled={saving} style={{ padding: 12, background: '#00d4aa', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer', fontWeight: 'bold' }}>
          {saving ? 'Saving...' : 'Save Profile'}
        </button>
      </form>
    </div>
  );
}
