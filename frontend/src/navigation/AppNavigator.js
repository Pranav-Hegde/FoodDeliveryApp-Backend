import React from 'react';
import { NavigationContainer, DefaultTheme } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import HomeScreen from '../screens/HomeScreen';
import { theme } from '../theme/theme';

const Stack = createStackNavigator();

const navigationTheme = {
  ...DefaultTheme,
  colors: {
    ...DefaultTheme.colors,
    background: theme.colors.background,
    card: theme.colors.surface,
    text: theme.colors.text,
    border: theme.colors.border,
    primary: theme.colors.primary,
  },
};

const AppNavigator = () => {
  return (
    <NavigationContainer theme={navigationTheme}>
      <Stack.Navigator screenOptions={{ headerShown: false }}>
        <Stack.Screen name="Home" component={HomeScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default AppNavigator;
