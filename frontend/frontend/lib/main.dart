import 'package:flutter/material.dart';
import 'package:frontend/modules/home/home_controller.dart';
import 'package:frontend/modules/home/patient/details/details_controller.dart';
import 'package:frontend/modules/home/patient/update/update_controller.dart';
import 'package:provider/provider.dart';
import 'app/app_widget.dart';

void main() {
  runApp(
    MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => PacienteController()),
        ChangeNotifierProvider(create: (_) => PacienteDetailsController()),
        ChangeNotifierProvider(create: (_) => PacienteUpdateController()),
      ],
      child: const AppWidget(),
    ),
  );
}
