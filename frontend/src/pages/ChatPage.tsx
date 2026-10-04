import { useState, useRef, useEffect } from 'react';
import api from '../api';

interface Source {
  documentTitle: string;
  sectionTitle: string | null;
  sourceName: string;
  relevanceScore: number;
  contentSnippet: string;
}

interface ReasoningTrace {
  intent: string;
  contextSummary: string;
  evidenceSummary: string;
  calculationSummary: string | null;
  decision: string;
}

interface ComplianceCheck {
  checkType: string;
  status: string;
  riskLevel: string;
  issues: string | null;
}

interface AgentContribution {
  agentName: string;
  contribution: string;
}

interface AiMetadata {
  sources?: Source[];
  reasoningTrace?: ReasoningTrace;
  complianceChecks?: ComplianceCheck[];
  agentsUsed?: AgentContribution[];
}

interface Message {
  id: string;
  role: 'USER' | 'AI';
  content: string;
  metadata?: AiMetadata;
}

const SUGGESTED = [
  'What is an emergency fund and how much should I save?',
  'I earn ₹50,000/month and spend ₹30,000. How should I start saving?',
  'What is a SIP and how does it work?',
  'How does Section 80C save income tax?',
];

const statusColor: Record<string, string> = {
  PASS: '#00a86b', MODIFY: '#e6a23c', BLOCK: '#ef4444', ESCALATE: '#8b5cf6',
};

function Accordion({ title, children }: { title: string; children: React.ReactNode }) {
  const [open, setOpen] = useState(false);
  return (
    <div style={{ marginTop: 8, border: '1px solid #e8e8e8', borderRadius: 8, overflow: 'hidden', fontSize: 13 }}>
      <div
        onClick={() => setOpen(!open)}
        style={{ padding: '7px 12px', background: '#f8f9fa', cursor: 'pointer', fontWeight: 600, color: '#555', display: 'flex', justifyContent: 'space-between' }}
      >
        <span>{title}</span>
        <span style={{ color: '#aaa' }}>{open ? '▾' : '▸'}</span>
      </div>
      {open && (
        <div style={{ padding: '10px 14px', background: 'white', color: '#444', lineHeight: 1.6 }}>
          {children}
        </div>
      )}
    </div>
  );
}

export default function ChatPage() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const send = async (text: string) => {
    if (!text.trim() || loading) return;
    const userMsg: Message = { id: Date.now().toString(), role: 'USER', content: text };
    setMessages(prev => [...prev, userMsg]);
    setInput('');
    setLoading(true);

    try {
      const res = await api.post('/api/conversations/chat', { message: text });
      setMessages(prev => [...prev, {
        id: (Date.now() + 1).toString(),
        role: 'AI',
        content: res.data.content,
        metadata: {
          sources: res.data.sources,
          reasoningTrace: res.data.reasoningTrace,
          complianceChecks: res.data.complianceChecks,
          agentsUsed: res.data.agentsUsed,
        }
      }]);
    } catch (err: any) {
      const errMsg = err.response?.data?.error || 'Sorry, I encountered an error. Please try again.';
      setMessages(prev => [...prev, { id: (Date.now() + 1).toString(), role: 'AI', content: errMsg }]);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    send(input);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: 'calc(100vh - 60px)', background: 'white', borderRadius: 12, boxShadow: '0 4px 12px rgba(0,0,0,0.06)', overflow: 'hidden' }}>

      {/* Header */}
      <div style={{ padding: '14px 20px', background: '#1a1a2e', color: 'white', display: 'flex', alignItems: 'center', gap: 12 }}>
        <div style={{ width: 36, height: 36, borderRadius: '50%', background: '#00d4aa', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: 18 }}>🤖</div>
        <div>
          <div style={{ fontWeight: 700, color: '#00d4aa', fontSize: 16 }}>TREZI AI Assistant</div>
          <div style={{ fontSize: 12, color: '#aaa' }}>Powered by Gemini · Grounded by RAG · Financial Literacy</div>
        </div>
      </div>

      {/* Messages */}
      <div style={{ flex: 1, padding: '20px', overflowY: 'auto', background: '#f8f9fa' }}>

        {messages.length === 0 && (
          <div>
            <div style={{ textAlign: 'center', marginBottom: 30, paddingTop: 20 }}>
              <div style={{ fontSize: 40, marginBottom: 10 }}>💡</div>
              <h3 style={{ color: '#1a1a2e', margin: '0 0 6px 0' }}>What would you like to learn today?</h3>
              <p style={{ color: '#888', fontSize: 14, margin: 0 }}>Ask me anything about personal finance, investing, or budgeting.</p>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 10, maxWidth: 560, margin: '0 auto' }}>
              {SUGGESTED.map((s, i) => (
                <button key={i} onClick={() => send(s)} style={{
                  padding: '12px 14px', background: 'white', border: '1px solid #e0e0e0',
                  borderRadius: 10, cursor: 'pointer', textAlign: 'left', fontSize: 13,
                  color: '#444', lineHeight: 1.4, boxShadow: '0 1px 4px rgba(0,0,0,0.04)',
                }}>
                  {s}
                </button>
              ))}
            </div>
          </div>
        )}

        {messages.map(msg => (
          <div key={msg.id} style={{ marginBottom: 18, display: 'flex', flexDirection: 'column', alignItems: msg.role === 'USER' ? 'flex-end' : 'flex-start' }}>
            <div style={{
              maxWidth: '78%', padding: '12px 16px', borderRadius: msg.role === 'USER' ? '18px 18px 4px 18px' : '18px 18px 18px 4px',
              background: msg.role === 'USER' ? '#00d4aa' : 'white',
              color: msg.role === 'USER' ? 'white' : '#222',
              boxShadow: '0 2px 6px rgba(0,0,0,0.07)',
              border: msg.role === 'AI' ? '1px solid #eee' : 'none',
            }}>
              <div style={{ lineHeight: 1.6, whiteSpace: 'pre-wrap' }}>{msg.content}</div>

              {msg.role === 'AI' && msg.metadata && (
                <div style={{ marginTop: 10 }}>

                  {/* Sources */}
                  {msg.metadata.sources && msg.metadata.sources.length > 0 && (
                    <Accordion title={`📚 Sources (${msg.metadata.sources.length} retrieved)`}>
                      {msg.metadata.sources.map((s, i) => (
                        <div key={i} style={{ marginBottom: 8, paddingBottom: 8, borderBottom: i < msg.metadata!.sources!.length - 1 ? '1px solid #f0f0f0' : 'none' }}>
                          <div style={{ fontWeight: 600, color: '#1a1a2e', fontSize: 13 }}>{s.documentTitle}</div>
                          {s.sectionTitle && <div style={{ color: '#888', fontSize: 12 }}>{s.sectionTitle}</div>}
                          <div style={{ color: '#666', fontSize: 12, marginTop: 3, fontStyle: 'italic' }}>"{s.contentSnippet}"</div>
                          <div style={{ color: '#00a86b', fontSize: 11, marginTop: 2 }}>
                            Relevance: {(Number(s.relevanceScore) * 100).toFixed(0)}% · {s.sourceName}
                          </div>
                        </div>
                      ))}
                    </Accordion>
                  )}

                  {/* Reasoning Trace */}
                  {msg.metadata.reasoningTrace && (
                    <Accordion title="🧠 Reasoning Trace">
                      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 12 }}>
                        <tbody>
                          {[
                            ['Intent', msg.metadata.reasoningTrace.intent],
                            ['Context', msg.metadata.reasoningTrace.contextSummary],
                            ['Evidence', msg.metadata.reasoningTrace.evidenceSummary],
                            ...(msg.metadata.reasoningTrace.calculationSummary ? [['Calculation', msg.metadata.reasoningTrace.calculationSummary]] : []),
                            ['Decision', msg.metadata.reasoningTrace.decision],
                          ].map(([k, v], i) => (
                            <tr key={i} style={{ borderBottom: '1px solid #f5f5f5' }}>
                              <td style={{ padding: '5px 8px 5px 0', fontWeight: 700, color: '#666', verticalAlign: 'top', whiteSpace: 'nowrap', paddingRight: 16 }}>{k}</td>
                              <td style={{ padding: '5px 0', color: '#444' }}>{v}</td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </Accordion>
                  )}

                  {/* Compliance */}
                  {msg.metadata.complianceChecks && msg.metadata.complianceChecks.length > 0 && (
                    <Accordion title="🛡️ Compliance Checks">
                      {msg.metadata.complianceChecks.map((c, i) => (
                        <div key={i} style={{ display: 'flex', alignItems: 'center', gap: 8, marginBottom: 4 }}>
                          <span style={{
                            padding: '2px 8px', borderRadius: 10, fontSize: 11, fontWeight: 700,
                            background: `${statusColor[c.status] || '#888'}22`,
                            color: statusColor[c.status] || '#888',
                          }}>{c.status}</span>
                          <span style={{ fontSize: 12, color: '#555' }}>{c.checkType.replace('_', ' ')}</span>
                          {c.issues && <span style={{ fontSize: 12, color: '#e06c75' }}>— {c.issues}</span>}
                        </div>
                      ))}
                    </Accordion>
                  )}

                  {/* Agents */}
                  {msg.metadata.agentsUsed && msg.metadata.agentsUsed.length > 0 && (
                    <div style={{ marginTop: 6, display: 'flex', gap: 6, flexWrap: 'wrap' }}>
                      {msg.metadata.agentsUsed.map((a, i) => (
                        <span key={i} style={{
                          fontSize: 11, padding: '2px 8px', borderRadius: 10,
                          background: '#1a1a2e11', color: '#555', fontWeight: 600,
                        }}>
                          ⚙ {a.agentName}
                        </span>
                      ))}
                    </div>
                  )}

                </div>
              )}
            </div>
          </div>
        ))}

        {loading && (
          <div style={{ display: 'flex', alignItems: 'center', gap: 8, color: '#888', fontSize: 14 }}>
            <div style={{ width: 36, height: 36, borderRadius: '50%', background: '#00d4aa22', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>🤖</div>
            <div style={{ background: 'white', padding: '10px 16px', borderRadius: '18px 18px 18px 4px', border: '1px solid #eee' }}>
              Thinking…
            </div>
          </div>
        )}
        <div ref={messagesEndRef} />
      </div>

      {/* Input */}
      <div style={{ padding: '14px 16px', background: 'white', borderTop: '1px solid #eee' }}>
        <form onSubmit={handleSubmit} style={{ display: 'flex', gap: 10 }}>
          <input
            type="text"
            value={input}
            onChange={e => setInput(e.target.value)}
            placeholder="Ask about budgeting, saving, investing, tax, insurance…"
            disabled={loading}
            style={{
              flex: 1, padding: '11px 16px', border: '1px solid #ddd', borderRadius: 24,
              outline: 'none', fontSize: 15, background: loading ? '#f5f5f5' : 'white',
            }}
          />
          <button
            type="submit"
            disabled={loading || !input.trim()}
            style={{
              background: (loading || !input.trim()) ? '#ccc' : '#00d4aa',
              color: 'white', border: 'none', borderRadius: 24,
              padding: '0 22px', fontWeight: 700, cursor: (loading || !input.trim()) ? 'default' : 'pointer',
              fontSize: 14,
            }}
          >
            Send
          </button>
        </form>
      </div>
    </div>
  );
}
