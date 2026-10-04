import { useState, useEffect } from 'react';
import api from '../api';

interface Question {
  id: string;
  questionText: string;
  topic: string;
  difficulty: string;
  options: string[];
}

interface AssessmentResult {
  assessmentId: string;
  overallScore: number;
  budgetingScore: number;
  savingScore: number;
  investmentScore: number;
  riskScore: number;
  digitalFinanceScore: number;
  taxScore: number;
  literacyLevel: string;
  completedAt: string;
}

const topicColors: Record<string, string> = {
  BUDGETING: '#00d4aa',
  SAVING: '#3b82f6',
  INVESTING: '#8b5cf6',
  RISK: '#f59e0b',
  DIGITAL_FINANCE: '#10b981',
  TAX: '#ef4444',
};

export default function AssessmentPage() {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [answers, setAnswers] = useState<Record<string, string>>({});
  const [result, setResult] = useState<AssessmentResult | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/assessments/latest-result')
      .then(res => {
        if (res.data) {
          setResult(res.data);
          setLoading(false);
        } else {
          fetchQuestions();
        }
      })
      .catch(() => fetchQuestions());
  }, []);

  const fetchQuestions = () => {
    api.get('/api/assessments/questions')
      .then(res => setQuestions(res.data))
      .catch(() => setError('Could not load questions. Please refresh.'))
      .finally(() => setLoading(false));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    setError('');
    // Backend expects: { responses: [{ questionId, selectedAnswer }] }
    const responses = Object.keys(answers).map(questionId => ({
      questionId,
      selectedAnswer: answers[questionId],
    }));
    try {
      const res = await api.post('/api/assessments/submit', { responses });
      setResult(res.data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Failed to submit. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <div style={{ padding: 40, textAlign: 'center', color: '#888' }}>Loading assessment…</div>;

  if (result) {
    const scores = [
      { label: 'Budgeting', value: result.budgetingScore, topic: 'BUDGETING' },
      { label: 'Saving', value: result.savingScore, topic: 'SAVING' },
      { label: 'Investing', value: result.investmentScore, topic: 'INVESTING' },
      { label: 'Risk', value: result.riskScore, topic: 'RISK' },
      { label: 'Digital Finance', value: result.digitalFinanceScore, topic: 'DIGITAL_FINANCE' },
      { label: 'Tax', value: result.taxScore, topic: 'TAX' },
    ];
    const levelColor = result.literacyLevel === 'ADVANCED' ? '#00d4aa' :
                       result.literacyLevel === 'INTERMEDIATE' ? '#f59e0b' : '#ef4444';

    return (
      <div style={{ background: 'white', padding: 32, borderRadius: 10, boxShadow: '0 2px 8px rgba(0,0,0,0.07)', maxWidth: 640 }}>
        <h2 style={{ margin: '0 0 6px 0', color: '#1a1a2e', fontSize: 22 }}>Assessment Result</h2>
        <div style={{ textAlign: 'center', padding: '28px 0 20px' }}>
          <div style={{ fontSize: 64, fontWeight: 900, color: '#00d4aa', lineHeight: 1 }}>
            {Number(result.overallScore).toFixed(0)}%
          </div>
          <div style={{
            display: 'inline-block', marginTop: 12, padding: '5px 20px', borderRadius: 20,
            background: levelColor, color: 'white', fontWeight: 700, fontSize: 16,
          }}>
            {result.literacyLevel}
          </div>
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: 12, marginBottom: 24 }}>
          {scores.map(s => (
            <div key={s.topic} style={{
              textAlign: 'center', padding: '12px 8px', borderRadius: 8,
              background: '#f9f9f9', border: `2px solid ${topicColors[s.topic]}22`,
            }}>
              <div style={{ fontSize: 22, fontWeight: 800, color: topicColors[s.topic] }}>
                {Number(s.value).toFixed(0)}%
              </div>
              <div style={{ fontSize: 12, color: '#777', marginTop: 2 }}>{s.label}</div>
            </div>
          ))}
        </div>

        <p style={{ color: '#666', textAlign: 'center', marginBottom: 20, fontSize: 14 }}>
          Keep using TREZI to improve your financial literacy!
        </p>
        <div style={{ display: 'flex', gap: 10 }}>
          <button
            onClick={() => { setResult(null); fetchQuestions(); setAnswers({}); }}
            style={{ flex: 1, padding: 12, background: '#f0f0f0', border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 600 }}
          >
            Retake Assessment
          </button>
          <a
            href="/chat"
            style={{ flex: 1, padding: 12, background: '#00d4aa', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer', fontWeight: 600, textDecoration: 'none', textAlign: 'center', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
          >
            Ask TREZI about your results →
          </a>
        </div>
      </div>
    );
  }

  const answeredCount = Object.keys(answers).length;

  return (
    <div style={{ maxWidth: 720 }}>
      <div style={{ background: 'white', padding: 32, borderRadius: 10, boxShadow: '0 2px 8px rgba(0,0,0,0.07)', marginBottom: 20 }}>
        <h2 style={{ margin: '0 0 6px 0', color: '#1a1a2e', fontSize: 22 }}>Financial Literacy Assessment</h2>
        <p style={{ color: '#888', fontSize: 14, margin: '0 0 4px 0' }}>
          {questions.length} questions across 6 financial topics
        </p>
        {questions.length > 0 && (
          <p style={{ color: answeredCount === questions.length ? '#00d4aa' : '#888', fontSize: 13, margin: 0 }}>
            {answeredCount}/{questions.length} answered
          </p>
        )}
      </div>

      {error && (
        <div style={{ background: '#fee', color: '#c33', padding: 12, borderRadius: 6, marginBottom: 16, fontSize: 14 }}>{error}</div>
      )}

      {questions.length === 0 && !error ? (
        <div style={{ textAlign: 'center', padding: 40, color: '#888' }}>No questions available.</div>
      ) : (
        <form onSubmit={handleSubmit}>
          {questions.map((q, idx) => (
            <div key={q.id} style={{
              background: 'white', padding: 20, borderRadius: 10, marginBottom: 14,
              boxShadow: '0 2px 6px rgba(0,0,0,0.05)', border: answers[q.id] ? '2px solid #00d4aa22' : '2px solid transparent',
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: 12 }}>
                <p style={{ fontWeight: 600, margin: 0, lineHeight: 1.5, flex: 1, paddingRight: 12 }}>
                  {idx + 1}. {q.questionText}
                </p>
                <span style={{
                  fontSize: 11, fontWeight: 700, padding: '2px 8px', borderRadius: 10, flexShrink: 0,
                  background: `${topicColors[q.topic] || '#888'}22`,
                  color: topicColors[q.topic] || '#888',
                }}>
                  {q.topic.replace('_', ' ')}
                </span>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {(q.options || []).map((opt, oi) => {
                  const selected = answers[q.id] === opt;
                  return (
                    <label key={oi} style={{
                      display: 'flex', alignItems: 'center', gap: 10, cursor: 'pointer',
                      padding: '9px 14px', borderRadius: 6,
                      background: selected ? '#00d4aa18' : '#f9f9f9',
                      border: `1px solid ${selected ? '#00d4aa' : '#e8e8e8'}`,
                      transition: 'all 0.15s',
                    }}>
                      <input
                        type="radio"
                        name={`q-${q.id}`}
                        value={opt}
                        checked={selected}
                        onChange={() => setAnswers({ ...answers, [q.id]: opt })}
                        required
                        style={{ accentColor: '#00d4aa' }}
                      />
                      <span style={{ fontSize: 14 }}>{opt}</span>
                    </label>
                  );
                })}
              </div>
            </div>
          ))}

          <div style={{ position: 'sticky', bottom: 0, background: '#f5f7fa', padding: '16px 0' }}>
            <button
              type="submit"
              disabled={submitting || answeredCount < questions.length}
              style={{
                width: '100%', padding: 14, fontWeight: 700, fontSize: 15,
                background: (submitting || answeredCount < questions.length) ? '#aaa' : '#00d4aa',
                color: 'white', border: 'none', borderRadius: 8,
                cursor: (submitting || answeredCount < questions.length) ? 'default' : 'pointer',
              }}
            >
              {submitting ? 'Submitting…' : answeredCount < questions.length ? `Answer all questions (${answeredCount}/${questions.length})` : 'Submit Assessment'}
            </button>
          </div>
        </form>
      )}
    </div>
  );
}
