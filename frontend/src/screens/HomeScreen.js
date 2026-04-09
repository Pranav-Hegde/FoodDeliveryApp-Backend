import React, { useContext, useEffect, useMemo, useRef, useState } from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TextInput,
  TouchableOpacity,
  Image,
  SafeAreaView,
  ActivityIndicator,
  Dimensions,
  Animated,
} from 'react-native';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { AuthContext } from '../context/AuthContext';
import { getRestaurants } from '../api/restaurant';
import {
  ArrowRight,
  Bell,
  Bike,
  ChefHat,
  Clock3,
  LayoutDashboard,
  LogOut,
  MapPin,
  Search,
  ShoppingBag,
  Sparkles,
  Star,
  Store,
  Truck,
  UtensilsCrossed,
} from 'lucide-react-native';
import { theme } from '../theme/theme';

const { width } = Dimensions.get('window');
const cardWidth = Math.min(width - theme.layout.containerPadding * 2, theme.layout.maxWidth);

const CATEGORIES = [
  { id: '1', name: 'Pizza', accent: '#f97316' },
  { id: '2', name: 'Burger', accent: '#ef4444' },
  { id: '3', name: 'Sushi', accent: '#14b8a6' },
  { id: '4', name: 'Healthy', accent: '#84cc16' },
  { id: '5', name: 'Coffee', accent: '#a16207' },
  { id: '6', name: 'Dessert', accent: '#ec4899' },
];

const TOP_PICKS = [
  {
    id: 'fallback-1',
    name: 'Brick Oven Society',
    rating: 4.9,
    deliveryTime: '20-30 min',
    imageUrl: 'https://images.unsplash.com/photo-1513104890138-7c749659a591?q=80&w=1200&auto=format&fit=crop',
    cuisine: 'Woodfire Pizza',
  },
  {
    id: 'fallback-2',
    name: 'Garden Bowl Kitchen',
    rating: 4.7,
    deliveryTime: '18-24 min',
    imageUrl: 'https://images.unsplash.com/photo-1547592180-85f173990554?q=80&w=1200&auto=format&fit=crop',
    cuisine: 'Healthy Bowls',
  },
  {
    id: 'fallback-3',
    name: 'Midnight Noodles',
    rating: 4.8,
    deliveryTime: '24-32 min',
    imageUrl: 'https://images.unsplash.com/photo-1617093727343-374698b1b08d?q=80&w=1200&auto=format&fit=crop',
    cuisine: 'Asian Comfort',
  },
];

const HomeScreen = () => {
  const { user, logout } = useContext(AuthContext);
  const insets = useSafeAreaInsets();
  const [search, setSearch] = useState('');
  const [restaurants, setRestaurants] = useState([]);
  const [loadingRes, setLoadingRes] = useState(false);
  const fadeAnim = useRef(new Animated.Value(0)).current;
  const slideAnim = useRef(new Animated.Value(18)).current;

  useEffect(() => {
    Animated.parallel([
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: theme.animation.medium,
        useNativeDriver: true,
      }),
      Animated.timing(slideAnim, {
        toValue: 0,
        duration: theme.animation.medium,
        useNativeDriver: true,
      }),
    ]).start();
  }, [fadeAnim, slideAnim]);

  useEffect(() => {
    if (user?.role === 'ROLE_USER') {
      fetchRestaurants();
    }
  }, [user]);

  const fetchRestaurants = async () => {
    setLoadingRes(true);
    try {
      const data = await getRestaurants(user?.token);
      setRestaurants(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error('Fetch Restaurants Failed', err);
      setRestaurants([]);
    } finally {
      setLoadingRes(false);
    }
  };

  const visibleRestaurants = useMemo(() => {
    const source = restaurants.length > 0 ? restaurants : TOP_PICKS;
    const query = search.trim().toLowerCase();

    if (!query) return source;

    return source.filter((restaurant) => {
      const name = restaurant.name?.toLowerCase() || '';
      const cuisine = restaurant.cuisine?.toLowerCase() || '';
      return name.includes(query) || cuisine.includes(query);
    });
  }, [restaurants, search]);

  const renderCustomerHeader = () => (
    <>
      <Animated.View
        style={[
          styles.header,
          {
            paddingTop: insets.top + theme.spacing.sm,
            opacity: fadeAnim,
            transform: [{ translateY: slideAnim }],
          },
        ]}
      >
        <View>
          <Text style={styles.eyebrow}>Good evening</Text>
          <Text style={styles.heroTitle}>Find something worth looking forward to.</Text>
        </View>
        <TouchableOpacity style={styles.iconButton} activeOpacity={0.8}>
          <Bell size={20} color={theme.colors.text} />
        </TouchableOpacity>
      </Animated.View>

      <View style={styles.featuredContainer}>
        <FeaturedCard />
      </View>

      <View style={styles.locationRow}>
        <View style={styles.locationChip}>
          <MapPin size={16} color={theme.colors.primary} />
          <Text style={styles.locationText}>Delivering to Home, 123 Street</Text>
        </View>
      </View>

      <View style={styles.searchWrap}>
        <Search size={20} color={theme.colors.textSoft} />
        <TextInput
          placeholder="Search restaurants or cuisines"
          placeholderTextColor={theme.colors.textSoft}
          style={styles.searchInput}
          value={search}
          onChangeText={setSearch}
        />
      </View>

      <SectionHeader title="Gastronomical Cravings" />
      <FlatList
        data={CATEGORIES}
        horizontal
        keyExtractor={(item) => item.id}
        showsHorizontalScrollIndicator={false}
        contentContainerStyle={styles.categoryList}
        renderItem={({ item }) => <CategoryItem {...item} />}
      />

      <View style={styles.metricsRow}>
        <InfoCard label="Fastest delivery" value="18 min" icon={<Clock3 size={18} color={theme.colors.accent} />} />
        <InfoCard label="Open now" value="32 places" icon={<Store size={18} color={theme.colors.primary} />} />
      </View>

      <SectionHeader title="Trending" action={restaurants.length > 0 ? 'Live Data' : 'Preview Mode'} />
    </>
  );

  const renderRestaurantDashboard = () => (
    <FlatList
      data={[
        { id: '1', title: 'Prep team speed', value: 'On target' },
        { id: '2', title: 'Pending pickups', value: '4 riders nearby' },
        { id: '3', title: 'Customer satisfaction', value: '4.8 average' },
      ]}
      keyExtractor={(item) => item.id}
      contentContainerStyle={[styles.dashboardContent, { paddingTop: insets.top + theme.spacing.sm }]}
      ListHeaderComponent={
        <>
          <DashboardHero
            icon={<ChefHat size={22} color={theme.colors.white} />}
            title="Restaurant control room"
            subtitle="Track service quality, keep orders moving, and update the menu with less friction."
          />
          <View style={styles.dashboardStatsRow}>
            <DashboardStat title="Live orders" value="12" tone="primary" />
            <DashboardStat title="Today's revenue" value="$1,280" tone="success" />
          </View>
          <TouchableOpacity style={styles.primaryAction} activeOpacity={0.85}>
            <Text style={styles.primaryActionText}>Add menu item</Text>
            <ArrowRight size={18} color={theme.colors.white} />
          </TouchableOpacity>
          <Text style={styles.panelTitle}>Service snapshot</Text>
        </>
      }
      renderItem={({ item }) => <DashboardListItem label={item.title} value={item.value} />}
      ListFooterComponent={
        <>
          <View style={styles.panel}>
            <Text style={styles.panelTitle}>Current orders</Text>
            <OrderCard title="Paneer Tikka Combo" detail="2 items - ready in 9 min" amount="$24.00" />
            <OrderCard title="Veggie Burger Box" detail="1 item - waiting for pickup" amount="$14.00" />
          </View>
          <LogoutButton onPress={logout} />
        </>
      }
    />
  );

  const renderDeliveryDashboard = () => (
    <FlatList
      data={[
        { id: '1', restaurant: 'Burger Haven', detail: '2.5 km away - pickup in 6 min', pay: '+$8.00' },
        { id: '2', restaurant: 'Sushi Zen', detail: '3.1 km away - stacked route', pay: '+$11.50' },
      ]}
      keyExtractor={(item) => item.id}
      contentContainerStyle={[styles.dashboardContent, { paddingTop: insets.top + theme.spacing.sm }]}
      ListHeaderComponent={
        <>
          <DashboardHero
            icon={<Bike size={22} color={theme.colors.white} />}
            title="Delivery partner hub"
            subtitle="Stay online, spot nearby tasks quickly, and make route decisions at a glance."
          />
          <View style={styles.statusBanner}>
            <Text style={styles.statusLabel}>Status</Text>
            <Text style={styles.statusValue}>Online and ready for assignments</Text>
          </View>
          <Text style={styles.panelTitle}>Nearby tasks</Text>
        </>
      }
      renderItem={({ item }) => <TaskCard restaurant={item.restaurant} detail={item.detail} pay={item.pay} />}
      ListFooterComponent={
        <>
          <View style={styles.dashboardStatsRow}>
            <DashboardStat title="Completed today" value="9" tone="success" />
            <DashboardStat title="Earnings" value="$74" tone="primary" />
          </View>
          <LogoutButton onPress={logout} />
        </>
      }
    />
  );

  if (user?.role === 'ROLE_RESTAURANT') {
    return <SafeAreaView style={styles.container}>{renderRestaurantDashboard()}</SafeAreaView>;
  }

  if (user?.role === 'ROLE_DELIVERY') {
    return <SafeAreaView style={styles.container}>{renderDeliveryDashboard()}</SafeAreaView>;
  }

  return (
    <SafeAreaView style={styles.container}>
      <FlatList
        data={visibleRestaurants}
        keyExtractor={(item, index) => item.id || index.toString()}
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.customerListContent}
        ListHeaderComponent={renderCustomerHeader}
        renderItem={({ item }) => <ModernResCard item={item} />}
        ListEmptyComponent={
          !loadingRes ? (
            <View style={styles.emptyState}>
              <UtensilsCrossed size={44} color={theme.colors.textSoft} />
              <Text style={styles.emptyTitle}>No restaurants match that search</Text>
              <Text style={styles.emptyText}>Try a broader keyword or refresh the live restaurant feed.</Text>
              <TouchableOpacity onPress={fetchRestaurants} style={styles.secondaryButton} activeOpacity={0.85}>
                <Text style={styles.secondaryButtonText}>Refresh restaurants</Text>
              </TouchableOpacity>
            </View>
          ) : null
        }
        ListFooterComponent={loadingRes ? <ActivityIndicator size="large" color={theme.colors.primary} style={styles.loader} /> : null}
        initialNumToRender={5}
        maxToRenderPerBatch={5}
        windowSize={5}
        removeClippedSubviews
      />

      <View style={styles.bottomNav}>
        <NavIcon icon={<LayoutDashboard size={22} color={theme.colors.primary} />} label="Home" active />
        <NavIcon icon={<ShoppingBag size={22} color={theme.colors.textMuted} />} label="Orders" />
        <NavIcon icon={<Truck size={22} color={theme.colors.textMuted} />} label="Track" />
        <TouchableOpacity onPress={logout} style={styles.navItem} activeOpacity={0.8}>
          <LogOut size={22} color={theme.colors.textMuted} />
          <Text style={styles.navLabel}>Logout</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const FeaturedCard = () => (
  <View style={styles.featuredCard}>
    <View style={styles.featuredCopy}>
      <Text style={styles.featuredEyebrow}>Curated tonight</Text>
      <Text style={styles.featuredTitle}>Free delivery on your first two orders.</Text>
      <Text style={styles.featuredText}>A more scalable feed with better motion, spacing, and touch response.</Text>
    </View>
    <View style={styles.featuredIconWrap}>
      <Sparkles size={24} color={theme.colors.white} />
    </View>
  </View>
);

const CategoryItem = ({ name, accent }) => (
  <TouchableOpacity style={[styles.categoryCard, { backgroundColor: accent }]} activeOpacity={0.85}>
    <Text style={styles.categoryName}>{name}</Text>
    <Text style={styles.categoryHint}>Top picks</Text>
  </TouchableOpacity>
);

const ModernResCard = ({ item }) => {
  const scaleAnim = useRef(new Animated.Value(1)).current;

  const onPressIn = () => {
    Animated.spring(scaleAnim, {
      toValue: 0.96,
      useNativeDriver: true,
      speed: 18,
      bounciness: 4,
    }).start();
  };

  const onPressOut = () => {
    Animated.spring(scaleAnim, {
      toValue: 1,
      useNativeDriver: true,
      speed: 18,
      bounciness: 4,
    }).start();
  };

  return (
    <TouchableOpacity activeOpacity={0.9} onPressIn={onPressIn} onPressOut={onPressOut}>
      <Animated.View style={[styles.resCard, { transform: [{ scale: scaleAnim }] }]}>
        <Image
          source={{
            uri:
              item.imageUrl ||
              'https://images.unsplash.com/photo-1552566626-52f8b828add9?q=80&w=1200&auto=format&fit=crop',
          }}
          style={styles.resImage}
        />
        <View style={styles.ratingBadge}>
          <Star size={12} color={theme.colors.white} fill={theme.colors.white} />
          <Text style={styles.ratingText}>{item.rating || '4.6'}</Text>
        </View>
        <View style={styles.resInfo}>
          <View style={styles.resTitleRow}>
            <View style={{ flex: 1 }}>
              <Text style={styles.resName}>{item.name}</Text>
              <Text style={styles.resCuisine}>{item.cuisine || 'Chef curated meals'}</Text>
            </View>
            <ArrowRight size={18} color={theme.colors.textMuted} />
          </View>
          <View style={styles.metaRow}>
            <MetaChip icon={<Clock3 size={14} color={theme.colors.textMuted} />} label={item.deliveryTime || '20-30 min'} />
            <MetaChip icon={<Truck size={14} color={theme.colors.textMuted} />} label="Free delivery" />
          </View>
        </View>
      </Animated.View>
    </TouchableOpacity>
  );
};

const SectionHeader = ({ title, action }) => (
  <View style={styles.sectionHeader}>
    <Text style={styles.sectionTitle}>{title}</Text>
    {action ? <Text style={styles.sectionAction}>{action}</Text> : null}
  </View>
);

const InfoCard = ({ label, value, icon }) => (
  <View style={styles.infoCard}>
    <View style={styles.infoIcon}>{icon}</View>
    <Text style={styles.infoValue}>{value}</Text>
    <Text style={styles.infoLabel}>{label}</Text>
  </View>
);

const MetaChip = ({ icon, label }) => (
  <View style={styles.metaChip}>
    {icon}
    <Text style={styles.metaChipText}>{label}</Text>
  </View>
);

const DashboardHero = ({ icon, title, subtitle }) => (
  <View style={styles.dashboardHero}>
    <View style={styles.dashboardHeroIcon}>{icon}</View>
    <Text style={styles.dashboardHeroTitle}>{title}</Text>
    <Text style={styles.dashboardHeroSubtitle}>{subtitle}</Text>
  </View>
);

const DashboardStat = ({ title, value, tone }) => (
  <View style={styles.dashboardStat}>
    <Text style={styles.dashboardStatTitle}>{title}</Text>
    <Text style={[styles.dashboardStatValue, tone === 'success' ? styles.successTone : styles.primaryTone]}>{value}</Text>
  </View>
);

const DashboardListItem = ({ label, value }) => (
  <View style={styles.listItem}>
    <Text style={styles.listItemLabel}>{label}</Text>
    <Text style={styles.listItemValue}>{value}</Text>
  </View>
);

const OrderCard = ({ title, detail, amount }) => (
  <View style={styles.panelCard}>
    <View style={{ flex: 1 }}>
      <Text style={styles.orderTitle}>{title}</Text>
      <Text style={styles.orderDetail}>{detail}</Text>
    </View>
    <Text style={styles.orderAmount}>{amount}</Text>
  </View>
);

const TaskCard = ({ restaurant, detail, pay }) => (
  <View style={styles.panelCard}>
    <View style={{ flex: 1 }}>
      <Text style={styles.orderTitle}>{restaurant}</Text>
      <Text style={styles.orderDetail}>{detail}</Text>
    </View>
    <Text style={styles.orderAmount}>{pay}</Text>
  </View>
);

const LogoutButton = ({ onPress }) => (
  <TouchableOpacity onPress={onPress} style={styles.logoutButton} activeOpacity={0.85}>
    <LogOut size={18} color={theme.colors.danger} />
    <Text style={styles.logoutButtonText}>Logout</Text>
  </TouchableOpacity>
);

const NavIcon = ({ icon, label, active }) => (
  <View style={styles.navItem}>
    {icon}
    <Text style={[styles.navLabel, active && styles.activeNavLabel]}>{label}</Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
  customerListContent: {
    paddingBottom: 140,
  },
  header: {
    paddingHorizontal: theme.layout.containerPadding,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
  },
  eyebrow: {
    ...theme.typography.small,
    color: theme.colors.accent,
    textTransform: 'uppercase',
    letterSpacing: 2,
    marginBottom: 10,
  },
  heroTitle: {
    ...theme.typography.h1,
    color: theme.colors.text,
    maxWidth: 260,
    lineHeight: 34,
  },
  iconButton: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: theme.colors.card,
    borderWidth: 1,
    borderColor: theme.colors.border,
    alignItems: 'center',
    justifyContent: 'center',
    ...theme.shadow.soft,
  },
  featuredContainer: {
    paddingHorizontal: theme.layout.containerPadding,
    marginTop: theme.spacing.lg,
  },
  featuredCard: {
    width: cardWidth,
    alignSelf: 'center',
    backgroundColor: theme.colors.primary,
    borderRadius: theme.radius.xl,
    padding: theme.spacing.lg,
    flexDirection: 'row',
    alignItems: 'center',
    ...theme.shadow.sunset,
  },
  featuredCopy: {
    flex: 1,
    paddingRight: theme.spacing.md,
  },
  featuredEyebrow: {
    ...theme.typography.small,
    color: 'rgba(255,255,255,0.75)',
    textTransform: 'uppercase',
    letterSpacing: 2,
    marginBottom: 8,
  },
  featuredTitle: {
    ...theme.typography.h2,
    color: theme.colors.white,
    lineHeight: 28,
  },
  featuredText: {
    ...theme.typography.body,
    color: 'rgba(255,255,255,0.82)',
    marginTop: 10,
    lineHeight: 21,
  },
  featuredIconWrap: {
    width: 54,
    height: 54,
    borderRadius: 27,
    backgroundColor: 'rgba(255,255,255,0.18)',
    alignItems: 'center',
    justifyContent: 'center',
  },
  locationRow: {
    paddingHorizontal: theme.layout.containerPadding,
    marginTop: theme.spacing.lg,
  },
  locationChip: {
    width: cardWidth,
    alignSelf: 'center',
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 14,
    paddingVertical: 12,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.pill,
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  locationText: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginLeft: 8,
  },
  searchWrap: {
    width: cardWidth,
    alignSelf: 'center',
    marginTop: theme.spacing.md,
    height: 58,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    ...theme.shadow.soft,
  },
  searchInput: {
    flex: 1,
    color: theme.colors.text,
    marginLeft: 12,
    fontSize: 15,
  },
  sectionHeader: {
    width: cardWidth,
    alignSelf: 'center',
    marginTop: theme.spacing.xl,
    marginBottom: theme.spacing.md,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  sectionTitle: {
    ...theme.typography.h2,
    color: theme.colors.text,
  },
  sectionAction: {
    ...theme.typography.body,
    color: theme.colors.primary,
  },
  categoryList: {
    paddingLeft: theme.layout.containerPadding,
    paddingRight: theme.spacing.sm,
  },
  categoryCard: {
    width: 126,
    height: 112,
    borderRadius: theme.radius.lg,
    padding: theme.spacing.md,
    justifyContent: 'space-between',
    marginRight: 12,
  },
  categoryName: {
    ...theme.typography.h3,
    color: theme.colors.white,
  },
  categoryHint: {
    ...theme.typography.body,
    color: 'rgba(255,255,255,0.82)',
  },
  metricsRow: {
    width: cardWidth,
    alignSelf: 'center',
    flexDirection: 'row',
    marginTop: theme.spacing.lg,
  },
  infoCard: {
    flex: 1,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
    padding: theme.spacing.md,
    marginRight: 12,
  },
  infoIcon: {
    width: 34,
    height: 34,
    borderRadius: 17,
    backgroundColor: theme.colors.primarySoft,
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: 12,
  },
  infoValue: {
    ...theme.typography.h3,
    color: theme.colors.text,
  },
  infoLabel: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginTop: 4,
  },
  resCard: {
    width: cardWidth,
    maxWidth: theme.layout.maxWidth,
    alignSelf: 'center',
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.xl,
    overflow: 'hidden',
    marginBottom: theme.spacing.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
    ...theme.shadow.medium,
  },
  resImage: {
    width: '100%',
    height: 160,
  },
  ratingBadge: {
    position: 'absolute',
    top: 16,
    right: 16,
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: theme.colors.glassBg,
    borderRadius: theme.radius.pill,
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderWidth: 1,
    borderColor: theme.colors.glassBorder,
  },
  ratingText: {
    color: theme.colors.white,
    fontWeight: '700',
    marginLeft: 6,
  },
  resInfo: {
    padding: theme.spacing.lg,
  },
  resTitleRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  resName: {
    ...theme.typography.h3,
    color: theme.colors.text,
  },
  resCuisine: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginTop: 6,
  },
  metaRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: theme.spacing.md,
  },
  metaChip: {
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: theme.colors.surface,
    borderRadius: theme.radius.pill,
    paddingHorizontal: 12,
    paddingVertical: 8,
    marginRight: 10,
    marginBottom: 8,
  },
  metaChipText: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginLeft: 8,
  },
  loader: {
    marginTop: theme.spacing.lg,
  },
  emptyState: {
    width: cardWidth,
    alignSelf: 'center',
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.xl,
    padding: theme.spacing.xl,
    alignItems: 'center',
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  emptyTitle: {
    ...theme.typography.h3,
    color: theme.colors.text,
    marginTop: 14,
  },
  emptyText: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    textAlign: 'center',
    marginTop: 8,
    lineHeight: 21,
  },
  secondaryButton: {
    marginTop: theme.spacing.lg,
    backgroundColor: theme.colors.primarySoft,
    borderRadius: theme.radius.pill,
    paddingHorizontal: 18,
    paddingVertical: 12,
  },
  secondaryButtonText: {
    ...theme.typography.body,
    color: theme.colors.primary,
  },
  bottomNav: {
    position: 'absolute',
    left: theme.layout.containerPadding,
    right: theme.layout.containerPadding,
    bottom: 20,
    backgroundColor: theme.colors.glassBg,
    borderRadius: 28,
    minHeight: 78,
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
    borderWidth: 1,
    borderColor: theme.colors.glassBorder,
  },
  navItem: {
    alignItems: 'center',
    justifyContent: 'center',
  },
  navLabel: {
    ...theme.typography.small,
    color: theme.colors.textMuted,
    marginTop: 4,
  },
  activeNavLabel: {
    color: theme.colors.primary,
  },
  dashboardContent: {
    paddingHorizontal: theme.layout.containerPadding,
    paddingBottom: theme.spacing.xxl,
  },
  dashboardHero: {
    backgroundColor: theme.colors.primaryDark,
    borderRadius: theme.radius.xl,
    padding: theme.spacing.xl,
    ...theme.shadow.sunset,
  },
  dashboardHeroIcon: {
    width: 48,
    height: 48,
    borderRadius: 24,
    backgroundColor: 'rgba(255,255,255,0.16)',
    alignItems: 'center',
    justifyContent: 'center',
    marginBottom: theme.spacing.md,
  },
  dashboardHeroTitle: {
    ...theme.typography.h1,
    color: theme.colors.white,
    lineHeight: 34,
  },
  dashboardHeroSubtitle: {
    ...theme.typography.body,
    color: 'rgba(255,255,255,0.8)',
    marginTop: 12,
    lineHeight: 21,
  },
  dashboardStatsRow: {
    flexDirection: 'row',
    marginTop: theme.spacing.lg,
  },
  dashboardStat: {
    flex: 1,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.lg,
    padding: theme.spacing.lg,
    marginRight: 12,
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  dashboardStatTitle: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginBottom: 8,
  },
  dashboardStatValue: {
    ...theme.typography.h1,
  },
  primaryTone: {
    color: theme.colors.primary,
  },
  successTone: {
    color: theme.colors.success,
  },
  primaryAction: {
    marginTop: theme.spacing.lg,
    backgroundColor: theme.colors.primary,
    borderRadius: theme.radius.lg,
    paddingHorizontal: 20,
    paddingVertical: 18,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  primaryActionText: {
    ...theme.typography.h3,
    color: theme.colors.white,
  },
  panel: {
    marginTop: theme.spacing.lg,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.xl,
    padding: theme.spacing.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  panelTitle: {
    ...theme.typography.h2,
    color: theme.colors.text,
    marginTop: theme.spacing.lg,
    marginBottom: theme.spacing.sm,
  },
  listItem: {
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.lg,
    padding: theme.spacing.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
    marginBottom: 12,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  listItemLabel: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
  },
  listItemValue: {
    ...theme.typography.body,
    color: theme.colors.text,
  },
  panelCard: {
    backgroundColor: theme.colors.surface,
    borderRadius: theme.radius.lg,
    padding: theme.spacing.md,
    marginTop: 12,
    flexDirection: 'row',
    alignItems: 'center',
  },
  orderTitle: {
    ...theme.typography.h3,
    color: theme.colors.text,
  },
  orderDetail: {
    ...theme.typography.body,
    color: theme.colors.textMuted,
    marginTop: 4,
  },
  orderAmount: {
    ...theme.typography.h3,
    color: theme.colors.primary,
    marginLeft: 12,
  },
  statusBanner: {
    marginTop: theme.spacing.lg,
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.lg,
    padding: theme.spacing.lg,
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  statusLabel: {
    ...theme.typography.small,
    color: theme.colors.accent,
    textTransform: 'uppercase',
    letterSpacing: 2,
  },
  statusValue: {
    ...theme.typography.h3,
    color: theme.colors.text,
    marginTop: 8,
  },
  logoutButton: {
    marginTop: theme.spacing.xl,
    alignSelf: 'center',
    flexDirection: 'row',
    alignItems: 'center',
    backgroundColor: theme.colors.card,
    borderRadius: theme.radius.pill,
    paddingHorizontal: 18,
    paddingVertical: 12,
    borderWidth: 1,
    borderColor: theme.colors.border,
  },
  logoutButtonText: {
    ...theme.typography.body,
    color: theme.colors.danger,
    marginLeft: 8,
  },
});

export default HomeScreen;
