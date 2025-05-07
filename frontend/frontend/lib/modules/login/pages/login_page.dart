import 'package:flutter/material.dart';
import 'package:frontend/modules/login/widgets/login_form.dart';

class LoginPage extends StatelessWidget {
  const LoginPage({super.key});

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final bool isDesktop = MediaQuery.of(context).size.width >= 800;
    if (isDesktop) {
      return Scaffold(
        body: Row(
          children: [
            Container(
              width: size.width * 0.6,
              height: size.height,
              color: Colors.white,
              child: const Center(
                child: Padding(
                  padding: EdgeInsets.all(32.0),
                  child: LoginForm(), 
                ),
              ),
              
            ),
            Expanded(
              child: Image.asset(
                'assets/images/login_bg.png',
                fit: BoxFit.cover,
                height: size.height,
              ),
            ),
          ],
        ),
      );
    } else {
      // Layout para mobile
      return Scaffold(
        body: Stack(
          children: [
            // Imagem de fundo
            Positioned.fill(
              child: Image.asset(
                'assets/images/login_bg.png',
                fit: BoxFit.cover,
              ),
            ),
            // Overlay escuro para contraste
            Positioned.fill(
              child: Container(color: Colors.white.withValues(alpha: 0.6)),
            ),
            // Formulário centralizado
            Center(
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(24.0),
                child: Container(
                  padding: const EdgeInsets.all(24.0),
                  decoration: BoxDecoration(
                    // color: Colors.white.withValues(alpha:0.6),
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(16),
                  ),
                  child: LoginForm(),
                ),
              ),
            ),
          ],
        ),
      );
    }
  }
}
