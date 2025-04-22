import 'package:flutter/material.dart';

class AppTheme {
  // Defina suas cores personalizadas
  static const Color primaryColor = Color(0xFF6200EE);
  static const Color secondaryColor = Color(0xFF03DAC6);
  static const Color backgroundColor = Color(0xFFF5F5F5);
  static const Color textPrimaryColor = Color(0xFF000000);

  // Tema Claro
  static ThemeData lightTheme = ThemeData(
    primaryColor: primaryColor,
    scaffoldBackgroundColor: backgroundColor,
    appBarTheme: AppBarTheme(
      backgroundColor: primaryColor,
      iconTheme: IconThemeData(color: Colors.white),
    ),
    textTheme: TextTheme(
      bodyLarge: TextStyle(
        color: Colors.black,
      ), // Substituindo 'bodyText1' por 'bodyLarge'
      bodyMedium: TextStyle(
        color: Colors.black54,
      ), // Substituindo 'bodyText2' por 'bodyMedium'
    ),
    buttonTheme: ButtonThemeData(
      buttonColor: primaryColor,
      textTheme: ButtonTextTheme.primary,
    ),
    colorScheme: ColorScheme.light(
      primary: primaryColor,
      secondary: secondaryColor,
      surface: backgroundColor, // Substituindo 'background' por 'surface'
    ),
  );

  // Tema Escuro
  static ThemeData darkTheme = ThemeData(
    brightness: Brightness.dark,
    primaryColor: primaryColor,
    scaffoldBackgroundColor: Colors.black,
    appBarTheme: AppBarTheme(
      backgroundColor: primaryColor,
      iconTheme: IconThemeData(color: Colors.white),
    ),
    textTheme: TextTheme(
      bodyLarge: TextStyle(
        color: Colors.white,
      ), // Substituindo 'bodyText1' por 'bodyLarge'
      bodyMedium: TextStyle(
        color: Colors.grey,
      ), // Substituindo 'bodyText2' por 'bodyMedium'
    ),
    buttonTheme: ButtonThemeData(
      buttonColor: primaryColor,
      textTheme: ButtonTextTheme.primary,
    ),
    colorScheme: ColorScheme.dark(
      primary: primaryColor,
      secondary: secondaryColor,
      surface: Colors.black, // Substituindo 'background' por 'surface'
    ),
  );
}
