import { createContext, useContext, useState, ReactNode } from 'react';

interface AuthContextType {
  token: string | null;
  userId: string | null;
  email: string | null;
  login: (token: string, userId: string, email: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType>(null!);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('trezi_token'));
  const [userId, setUserId] = useState<string | null>(localStorage.getItem('trezi_userId'));
  const [email, setEmail] = useState<string | null>(localStorage.getItem('trezi_email'));

  const login = (token: string, userId: string, email: string) => {
    localStorage.setItem('trezi_token', token);
    localStorage.setItem('trezi_userId', userId);
    localStorage.setItem('trezi_email', email);
    setToken(token);
    setUserId(userId);
    setEmail(email);
  };

  const logout = () => {
    localStorage.removeItem('trezi_token');
    localStorage.removeItem('trezi_userId');
    localStorage.removeItem('trezi_email');
    setToken(null);
    setUserId(null);
    setEmail(null);
  };

  return (
    <AuthContext.Provider value={{ token, userId, email, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);
