import 'package:flutter/material.dart';
import '../app/routes/app_router.dart';
import '../shared/themes/app_theme.dart'; // Importando o tema

class AppWidget extends StatelessWidget {
  const AppWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      routerConfig: router,
      title: 'Projeto Monitoramento',
      theme: AppTheme.lightTheme,
    );
  }
}
