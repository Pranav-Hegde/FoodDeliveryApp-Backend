import { Platform } from 'react-native';

/**
 * THEME: "Sunset Onyx"
 * A high-end, contemporary palette for the new Foodly.
 */
export const theme = {
  colors: {
    // Core Neutrals
    background: '#09090b',    // Deep zinc black
    surface: '#18181b',       // Zinc-900 surface
    card: '#27272a',          // Zinc-800 card
    border: '#3f3f46',        // Zinc-700 border
    
    // Brand Colors (Sunset Gradient)
    primary: '#f97316',       // Vibrant Orange (Orange-500)
    primaryDark: '#c2410c',   // Deep Burnt Orange (Orange-700)
    primarySoft: 'rgba(249, 115, 22, 0.1)',
    secondary: '#ef4444',     // Crimson Red (Red-500)
    accent: '#fbbf24',        // Amber Gold
    
    // Functional
    success: '#10b981',       // Emerald
    warning: '#f59e0b',       // Amber
    danger: '#ef4444',        // Crimson
    info: '#3b82f6',          // Sky Blue
    
    // Typography
    text: '#fafafa',          // Neutral-50
    textMuted: '#a1a1aa',     // Zinc-400
    textSoft: '#71717a',      // Zinc-500
    white: '#ffffff',
    black: '#000000',
    
    // Glassmorphism
    glassBg: 'rgba(24, 24, 27, 0.7)',
    glassBorder: 'rgba(255, 255, 255, 0.1)',
  },
  
  spacing: {
    xs: 4,
    sm: 8,
    md: 16,
    lg: 24,
    xl: 32,
    xxl: 48,
    xxxl: 64,
  },
  
  radius: {
    xs: 8,
    sm: 12,
    md: 16,
    lg: 20,
    xl: 32,
    pill: 999,
  },
  
  typography: {
    h1: { fontSize: 28, fontWeight: '900' },
    h2: { fontSize: 22, fontWeight: '800' },
    h3: { fontSize: 18, fontWeight: '700' },
    body: { fontSize: 14, fontWeight: '500' },
    small: { fontSize: 12, fontWeight: '500' },
  },
  
  animation: {
    fast: 200,
    medium: 500,
    slow: 900,
  },
  
  layout: {
    containerPadding: 16,
    maxWidth: 500,
  },
  
  shadow: {
    soft: Platform.select({
      web: { boxShadow: '0 4px 12px rgba(0,0,0,0.4)' },
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 4 }, shadowOpacity: 0.4, shadowRadius: 8, elevation: 4 }
    }),
    float: Platform.select({
      web: { boxShadow: '0 12px 30px rgba(0,0,0,0.45)' },
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 12 }, shadowOpacity: 0.45, shadowRadius: 18, elevation: 10 }
    }),
    medium: Platform.select({
      web: { boxShadow: '0 8px 30px rgba(0,0,0,0.6)' },
      default: { shadowColor: '#000', shadowOffset: { width: 0, height: 8 }, shadowOpacity: 0.6, shadowRadius: 16, elevation: 8 }
    }),
    sunset: Platform.select({
      web: { boxShadow: '0 10px 40px rgba(249, 115, 22, 0.3)' },
      default: { shadowColor: '#f97316', shadowOffset: { width: 0, height: 10 }, shadowOpacity: 0.3, shadowRadius: 20, elevation: 12 }
    }),
  }
};
