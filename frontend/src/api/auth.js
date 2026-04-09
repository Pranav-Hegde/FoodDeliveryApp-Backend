import axios from 'axios';

const API_URL = 'http://localhost:8080/auth';

export const checkExistence = async (query) => {
  try {
    const response = await axios.get(`${API_URL}/check-existence`, {
      params: { query },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const register = async (userData) => {
  try {
    const response = await axios.post(`${API_URL}/register`, userData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};

export const login = async (identifier, password) => {
  try {
    const response = await axios.post(`${API_URL}/login`, {
      identifier,
      password,
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
