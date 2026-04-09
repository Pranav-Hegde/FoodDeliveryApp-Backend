import React, { createContext, useState, useEffect, useCallback } from 'react';
import AsyncStorage from '@react-native-async-storage/async-storage';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Load user from storage on app start
  useEffect(() => {
    const loadUser = async () => {
      try {
        const storedUser = await AsyncStorage.getItem('authUser');
        if (storedUser) {
          setUser(JSON.parse(storedUser));
        }
      } catch (e) {
        console.error('Failed to load user:', e);
        setError('Failed to restore session');
      } finally {
        setLoading(false);
      }
    };
    loadUser();
  }, []);

  const signup = useCallback(async (userData) => {
    try {
      setUser(userData);
      await AsyncStorage.setItem('authUser', JSON.stringify(userData));
    } catch (err) {
      console.error('Signup storage error:', err);
    }
  }, []);

  const login = useCallback(async (userData) => {
    try {
      setUser(userData);
      await AsyncStorage.setItem('authUser', JSON.stringify(userData));
    } catch (err) {
      console.error('Login storage error:', err);
    }
  }, []);

  const logout = useCallback(async () => {
    try {
      setUser(null);
      setError(null);
      await AsyncStorage.removeItem('authUser');
    } catch (err) {
      console.error('Logout error:', err);
    }
  }, []);

  const clearError = useCallback(() => {
    setError(null);
  }, []);

  return (
    <AuthContext.Provider
      value={{
        user,
        loading,
        error,
        login,
        signup,
        logout,
        clearError,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
