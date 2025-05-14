import 'package:flutter/material.dart';
import 'package:frontend/modules/home/home_content/home_content.dart';
import 'package:frontend/modules/home/patient/details/patient_details_page.dart';
import 'package:frontend/modules/home/patient/register/patient_register_page.dart';
import 'package:frontend/modules/home/patient/update/patient_update_page.dart';

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
      ],
    ),

    GoRoute(
      path: '/patient-register',
      builder: (context, state) => const PatientRegister(),
    ),
    // GoRoute(
    //   path: '/patient-update',
    //   builder: (context, state) => const PatientUpdate(),
    // ),
    GoRoute(
      path: '/patient-update/:id', // Agora aceita um ID
      builder: (context, state) {
        final String id = state.pathParameters['id']!;
        return PatientUpdate(id: id);
      },
    ),
    // GoRoute(
    //   path: '/patient-details',
    //   builder: (context, state) => const PatientDetails(),
    // ),
    GoRoute(
      path: '/patient-details/:id', // Agora aceita um parâmetro "id"
      builder: (context, state) {
        final String id = state.pathParameters['id']!; // Pegamos o ID da rota
        return PatientDetails(id: id); // Passamos o ID para a tela
      },
    ),
    GoRoute(path: '/login', builder: (context, state) => const LoginPage()),
  ],
);
