import React, { useState, useContext } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    ActivityIndicator,
    Alert,
    SafeAreaView,
    KeyboardAvoidingView,
    Platform,
    ScrollView,
    Dimensions,
} from 'react-native';
import { AuthContext } from '../context/AuthContext';
import { checkPhone, loginUser, registerUser } from '../api/auth';
import {
    ArrowRight,
    Phone,
    Lock,
    User,
    Truck,
    ChefHat,
    Eye,
    EyeOff,
    Sparkles,
    ArrowLeft,
} from 'lucide-react-native';
import { theme } from '../theme/theme';

const { width } = Dimensions.get('window');

const ROLE_OPTIONS = [
    { key: 'ROLE_USER', title: 'Customer', subtitle: 'Order food and track deliveries', icon: User },
    { key: 'ROLE_RESTAURANT', title: 'Partner', subtitle: 'Manage your menu and orders', icon: ChefHat },
    { key: 'ROLE_DELIVERY', title: 'Rider', subtitle: 'Earn by delivering happiness', icon: Truck },
];

const AuthScreen = () => {
    const { login } = useContext(AuthContext);
    const [phone, setPhone] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [role, setRole] = useState('ROLE_USER');
    const [loading, setLoading] = useState(false);
    const [step, setStep] = useState('phone'); // 'phone', 'role', 'password'
    const [isNewUser, setIsNewUser] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);

    const validatePhone = (phoneNumber) => {
        const phoneRegex = /^[0-9]{10,}$/;
        return phoneRegex.test(phoneNumber.replace(/\D/g, ''));
    };

    const handleContinue = async () => {
        if (!phone) return Alert.alert('Error', 'Please enter your phone number.');
        if (!validatePhone(phone)) return Alert.alert('Error', 'Please enter a valid phone number.');

        setLoading(true);
        try {
            const status = await checkPhone(phone);
            if (status.exists) {
                // Existing user - go to password login
                setIsNewUser(false);
                setRole(status.role);
                setStep('password');
            } else {
                // New user - go to role selection
                setIsNewUser(true);
                setStep('role');
            }
        } catch (err) {
            Alert.alert('Error', String(err) || 'Failed to check user');
        } finally {
            setLoading(false);
        }
    };

    const handleLogin = async () => {
        if (!password) return Alert.alert('Error', 'Please enter your password.');

        setLoading(true);
        try {
            const data = await loginUser(phone, password);
            login({ phone, role: data.role, token: data.token });
        } catch (err) {
            Alert.alert('Login Failed', String(err) || 'Invalid credentials');
        } finally {
            setLoading(false);
        }
    };

    const handleRegister = async () => {
        if (!password) return Alert.alert('Error', 'Please enter a password.');
        if (password.length < 6) return Alert.alert('Error', 'Password must be at least 6 characters.');
        if (password !== confirmPassword) return Alert.alert('Error', 'Passwords do not match.');

        setLoading(true);
        try {
            const data = await registerUser(phone, password, role);
            login({ phone, role: data.role, token: data.token });
        } catch (err) {
            Alert.alert('Registration Failed', String(err) || 'Failed to create account');
        } finally {
            setLoading(false);
        }
    };

    const renderStepPhone = () => (
        <View style={styles.card}>
            <Text style={styles.cardTitle}>Welcome</Text>
            <Text style={styles.cardSubtitle}>Enter your phone number to get started with Foodly.</Text>

            <Text style={styles.inputLabel}>Phone Number</Text>
            <View style={styles.inputGroup}>
                <View style={styles.inputWrapper}>
                    <Phone size={20} color={theme.colors.textSoft} style={styles.inputIcon} />
                    <TextInput
                        placeholder="Enter your phone number"
                        placeholderTextColor={theme.colors.textSoft}
                        value={phone}
                        onChangeText={setPhone}
                        style={styles.input}
                        keyboardType="phone-pad"
                        editable={!loading}
                    />
                </View>
            </View>

            <TouchableOpacity
                onPress={handleContinue}
                style={[styles.button, loading && styles.buttonDisabled]}
                disabled={loading}
            >
                {loading ? (
                    <ActivityIndicator color={theme.colors.white} />
                ) : (
                    <View style={styles.buttonContent}>
                        <Text style={styles.buttonText}>Continue</Text>
                        <ArrowRight size={20} color={theme.colors.white} style={{ marginLeft: 10 }} />
                    </View>
                )}
            </TouchableOpacity>
        </View>
    );

    const renderStepRole = () => (
        <View style={styles.card}>
            <TouchableOpacity style={styles.backButton} onPress={() => setStep('phone')}>
                <ArrowLeft size={18} color={theme.colors.textMuted} />
                <Text style={styles.backText}>Back</Text>
            </TouchableOpacity>

            <Text style={styles.cardTitle}>Join the community</Text>
            <Text style={styles.cardSubtitle}>Choose your role to customize your experience.</Text>

            <View style={styles.roleContainer}>
                {ROLE_OPTIONS.map(({ key, title, subtitle, icon: Icon }) => (
                    <RoleButton
                        key={key}
                        title={title}
                        subtitle={subtitle}
                        icon={<Icon size={20} color={role === key ? theme.colors.white : theme.colors.primaryDark} />}
                        selected={role === key}
                        onPress={() => setRole(key)}
                    />
                ))}
            </View>

            <TouchableOpacity
                onPress={() => setStep('password')}
                style={[styles.button, loading && styles.buttonDisabled]}
                disabled={loading}
            >
                {loading ? (
                    <ActivityIndicator color={theme.colors.white} />
                ) : (
                    <View style={styles.buttonContent}>
                        <Text style={styles.buttonText}>Continue</Text>
                        <ArrowRight size={20} color={theme.colors.white} style={{ marginLeft: 10 }} />
                    </View>
                )}
            </TouchableOpacity>
        </View>
    );

    const renderStepPassword = () => (
        <View style={styles.card}>
            <TouchableOpacity style={styles.backButton} onPress={() => setStep(isNewUser ? 'role' : 'phone')}>
                <ArrowLeft size={18} color={theme.colors.textMuted} />
                <Text style={styles.backText}>Back</Text>
            </TouchableOpacity>

            <Text style={styles.cardTitle}>{isNewUser ? 'Create password' : 'Sign in'}</Text>
            <Text style={styles.cardSubtitle}>
                {isNewUser
                    ? 'Set a secure password for your account.'
                    : 'Enter your password to continue.'}
            </Text>

            <Text style={styles.inputLabel}>Password</Text>
            <View style={styles.inputGroup}>
                <View style={styles.inputWrapper}>
                    <Lock size={20} color={theme.colors.textSoft} style={styles.inputIcon} />
                    <TextInput
                        placeholder="Enter password"
                        placeholderTextColor={theme.colors.textSoft}
                        value={password}
                        onChangeText={setPassword}
                        style={styles.input}
                        secureTextEntry={!showPassword}
                        editable={!loading}
                    />
                    <TouchableOpacity onPress={() => setShowPassword(!showPassword)} style={styles.eyeIcon}>
                        {showPassword ? (
                            <Eye size={20} color={theme.colors.textSoft} />
                        ) : (
                            <EyeOff size={20} color={theme.colors.textSoft} />
                        )}
                    </TouchableOpacity>
                </View>
            </View>

            {isNewUser && (
                <>
                    <Text style={styles.inputLabel}>Confirm Password</Text>
                    <View style={styles.inputGroup}>
                        <View style={styles.inputWrapper}>
                            <Lock size={20} color={theme.colors.textSoft} style={styles.inputIcon} />
                            <TextInput
                                placeholder="Confirm password"
                                placeholderTextColor={theme.colors.textSoft}
                                value={confirmPassword}
                                onChangeText={setConfirmPassword}
                                style={styles.input}
                                secureTextEntry={!showConfirmPassword}
                                editable={!loading}
                            />
                            <TouchableOpacity onPress={() => setShowConfirmPassword(!showConfirmPassword)} style={styles.eyeIcon}>
                                {showConfirmPassword ? (
                                    <Eye size={20} color={theme.colors.textSoft} />
                                ) : (
                                    <EyeOff size={20} color={theme.colors.textSoft} />
                                )}
                            </TouchableOpacity>
                        </View>
                    </View>

                    <Text style={styles.helpText}>Password must be at least 6 characters</Text>
                </>
            )}

            <TouchableOpacity
                onPress={isNewUser ? handleRegister : handleLogin}
                style={[styles.button, loading && styles.buttonDisabled]}
                disabled={loading}
            >
                {loading ? (
                    <ActivityIndicator color={theme.colors.white} />
                ) : (
                    <View style={styles.buttonContent}>
                        <Text style={styles.buttonText}>{isNewUser ? 'Create Account' : 'Sign In'}</Text>
                        <ArrowRight size={20} color={theme.colors.white} style={{ marginLeft: 10 }} />
                    </View>
                )}
            </TouchableOpacity>
        </View>
    );

    return (
        <SafeAreaView style={styles.container}>
            <KeyboardAvoidingView
                behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
                style={{ flex: 1 }}
            >
                <ScrollView contentContainerStyle={styles.scrollContent}>
                    <View style={styles.hero}>
                        <View style={styles.heroBadge}>
                            <Sparkles size={16} color={theme.colors.primaryDark} />
                            <Text style={styles.heroBadgeText}>Fast & Fresh Delivery</Text>
                        </View>
                        <Text style={styles.title}>Crave. Order. Devour.</Text>
                        <Text style={styles.subtitle}>
                            Experience premium food delivery at your fingertips. Quick, reliable, and delicious.
                        </Text>
                    </View>

                    {step === 'phone' && renderStepPhone()}
                    {step === 'role' && renderStepRole()}
                    {step === 'password' && renderStepPassword()}
                </ScrollView>
            </KeyboardAvoidingView>
        </SafeAreaView>
    );
};

const RoleButton = ({ title, subtitle, icon, selected, onPress }) => (
    <TouchableOpacity onPress={onPress} style={[styles.roleBtn, selected && styles.roleBtnSelected]}>
        <View style={[styles.roleIconWrap, selected && styles.roleIconWrapSelected]}>{icon}</View>
        <View style={styles.roleCopy}>
            <Text style={[styles.roleLabel, selected && styles.roleLabelSelected]}>{title}</Text>
            <Text style={[styles.roleSubtitle, selected && styles.roleSubtitleSelected]}>{subtitle}</Text>
        </View>
    </TouchableOpacity>
);

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: theme.colors.background },
    scrollContent: { padding: theme.spacing.lg, flexGrow: 1, justifyContent: 'center' },
    hero: {
        backgroundColor: theme.colors.primaryDark,
        borderRadius: theme.radius.xl,
        padding: theme.spacing.xl,
        marginBottom: theme.spacing.lg,
        ...theme.shadow.float,
    },
    heroBadge: {
        alignSelf: 'flex-start',
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: 'rgba(255,255,255,0.14)',
        borderRadius: theme.radius.pill,
        paddingHorizontal: 14,
        paddingVertical: 8,
        marginBottom: theme.spacing.md,
    },
    heroBadgeText: {
        color: theme.colors.white,
        fontWeight: '700',
        marginLeft: 8,
    },
    title: { fontSize: 32, fontWeight: '900', color: theme.colors.white, lineHeight: 40 },
    subtitle: { fontSize: 16, color: '#f5e5d7', marginTop: 12, lineHeight: 24 },
    card: {
        backgroundColor: theme.colors.surface,
        padding: theme.spacing.lg,
        borderRadius: theme.radius.xl,
        borderWidth: 1,
        borderColor: theme.colors.border,
        ...theme.shadow.soft,
    },
    cardTitle: { fontSize: 26, fontWeight: '800', color: theme.colors.text },
    cardSubtitle: {
        fontSize: 15,
        color: theme.colors.textMuted,
        marginTop: 8,
        marginBottom: theme.spacing.lg,
        lineHeight: 22,
    },
    backButton: { flexDirection: 'row', alignItems: 'center', marginBottom: 20 },
    backText: { marginLeft: 8, color: theme.colors.textMuted, fontWeight: '600' },
    roleContainer: { marginBottom: theme.spacing.md },
    roleBtn: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: theme.spacing.md,
        borderRadius: theme.radius.md,
        backgroundColor: theme.colors.card,
        borderWidth: 1,
        borderColor: theme.colors.border,
        marginBottom: 12,
    },
    roleBtnSelected: {
        backgroundColor: theme.colors.primary,
        borderColor: theme.colors.primary,
    },
    roleIconWrap: {
        width: 44,
        height: 44,
        borderRadius: 22,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: theme.colors.primarySoft,
        marginRight: 14,
    },
    roleIconWrapSelected: {
        backgroundColor: 'rgba(255,255,255,0.18)',
    },
    roleCopy: { flex: 1 },
    roleLabel: { fontSize: 16, fontWeight: '700', color: theme.colors.text },
    roleLabelSelected: { color: theme.colors.white },
    roleSubtitle: { fontSize: 13, color: theme.colors.textMuted, marginTop: 4 },
    roleSubtitleSelected: { color: '#fde8d2' },
    inputLabel: {
        fontSize: 14,
        fontWeight: '700',
        color: theme.colors.text,
        marginBottom: 8,
    },
    inputGroup: { marginBottom: theme.spacing.md },
    inputWrapper: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: theme.colors.card,
        borderRadius: theme.radius.md,
        paddingHorizontal: 15,
        borderWidth: 1,
        borderColor: theme.colors.border,
    },
    inputIcon: { marginRight: 10 },
    eyeIcon: { padding: 8, marginRight: -8 },
    input: { flex: 1, height: 56, fontSize: 16, color: theme.colors.text },
    helpText: {
        fontSize: 12,
        color: theme.colors.textSoft,
        marginTop: -8,
        marginBottom: theme.spacing.md,
    },
    button: {
        backgroundColor: theme.colors.primary,
        minHeight: 58,
        borderRadius: theme.radius.md,
        alignItems: 'center',
        justifyContent: 'center',
        marginTop: 10,
        ...theme.shadow.soft,
    },
    buttonDisabled: {
        opacity: 0.6,
    },
    buttonContent: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center' },
    buttonText: { color: theme.colors.white, fontSize: 17, fontWeight: '800' },
});

export default AuthScreen;
