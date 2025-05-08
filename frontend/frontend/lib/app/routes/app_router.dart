import 'package:frontend/modules/home/settings/settings_page.dart';
import 'package:go_router/go_router.dart';
import '../../modules/login/pages/login_page.dart';
import '../../modules/home/home_page.dart';

final router = GoRouter(
  initialLocation: '/home',
  routes: [
    GoRoute(path: '/home', builder: (context, state) => const HomePage()),
    GoRoute(path: '/login', builder: (context, state) => const LoginPage()),
    GoRoute(path: '/settings', builder: (context, state) => const SettingsPage()),
  ],
);
