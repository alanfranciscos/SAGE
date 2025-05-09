import 'package:flutter/material.dart';
import 'package:frontend/modules/home/home_content/home_content.dart';
import 'package:frontend/modules/home/patient/details/patient_details_page.dart';
import 'package:frontend/modules/home/patient/register/patient_register_page.dart';
import 'package:go_router/go_router.dart';
import '../../modules/home/home_page.dart';
import '../../modules/home/settings/settings_page.dart';
import '../../modules/login/pages/login_page.dart';


final router = GoRouter(
  initialLocation: '/home',
  routes: [
    ShellRoute(
      builder: (context, state, child) {
        return HomePage(child: child);
      },
      routes: [
        GoRoute(
          path: '/home',
          builder: (context, state) {
            return const HomeContent();
          },
        ),
        GoRoute(
          path: '/settings',
          builder: (context, state) => const SettingsPage(),
        ),
        GoRoute(
          path: '/profile',
          builder: (context, state) => const Center(child: Text('Perfil')),
        ),
        GoRoute(
          path:'/patient-register',
          builder: (context, state) => const PatientRegister(),
        ),
        GoRoute(
          path:'/patient-details',
          builder: (context, state) => const PatientDetails(),
        ),
      ],
    ),


    GoRoute(path: '/login', builder: (context, state) => const LoginPage()),
  ],
);



