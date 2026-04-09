import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/restaurants';

export const getRestaurants = async (token) => {
  try {
    const response = await axios.get(API_BASE_URL, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error.message;
  }
};
