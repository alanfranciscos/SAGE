import 'package:go_router/go_router.dart';
import '../../modules/login/login_page.dart';
import '../../modules/home/home_page.dart';

final router = GoRouter(
  initialLocation: '/home',
  routes: [
    GoRoute(path: '/home', builder: (context, state) => const HomePage()),
    GoRoute(path: '/login', builder: (context, state) => const LoginPage()),
  ],
);
