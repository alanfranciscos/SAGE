import 'package:flutter/material.dart';
import 'package:frontend/modules/home/widgets/patient_card.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';

class HomeContent extends StatelessWidget {
  const HomeContent({super.key});

  @override
  Widget build(BuildContext context) {
    //aqui simula a lista
    final List<Map<String, String>> mockPatients = List.generate(17, (index) {
      return {
        'name': 'Paciente ${index + 1}',
        'number': 'Casa: ${100 + index}',
        'imagePath': 'assets/images/login_bg.png',
      };
    });

    final bool isDesktop = MediaQuery.of(context).size.width >= 800;

    return SingleChildScrollView(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Visão geral dos pacientes monitorados',
            style: TextStyle(
              fontSize: 24,
              fontWeight: FontWeight.bold,
              color: AppTheme.textDarkColor,
            ),
          ),
          const SizedBox(height: 40),
          LayoutBuilder(
            builder: (context, constraints) {
              final isMobile = constraints.maxWidth < 800;

              if (isMobile) {
                return Column(
                  crossAxisAlignment: CrossAxisAlignment.stretch,
                  children: [
                    SizedBox(
                      child: TextField(
                        decoration: InputDecoration(
                          hintText: 'Pesquisar paciente',
                          hintStyle: TextStyle(color: AppTheme.textDarkColor),
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: BorderSide(
                              color: AppTheme.textDarkColor,
                              width: 1,
                            ),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: BorderSide(
                              color: AppTheme.primaryColor,
                              width: 2,
                            ),
                          ),
                          prefixIcon: const Icon(Icons.search),
                        ),
                      ),
                    ),
                    const SizedBox(height: 12),
                    ElevatedButton.icon(
                      onPressed: () {
                        context.go('/patient-register');
                      },
                      icon: const Icon(
                        Icons.add,
                        color: AppTheme.textDarkColor,
                      ),
                      label: const Text('Cadastrar paciente'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.tertiaryColor,
                        foregroundColor: AppTheme.textDarkColor,
                        padding: const EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 16,
                        ),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                    ),
                  ],
                );
              } else {
                return Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    SizedBox(
                      width: 300,
                      child: TextField(
                        decoration: InputDecoration(
                          hintText: 'Pesquisar paciente',
                          hintStyle: TextStyle(color: AppTheme.textDarkColor),
                          enabledBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: BorderSide(
                              color: AppTheme.textDarkColor,
                              width: 1,
                            ),
                          ),
                          focusedBorder: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(8),
                            borderSide: BorderSide(
                              color: AppTheme.primaryColor,
                              width: 2,
                            ),
                          ),
                          prefixIcon: const Icon(Icons.search),
                        ),
                      ),
                    ),
                    ElevatedButton.icon(
                      onPressed: () {
                        context.go('/patient-register');
                      },
                      icon: const Icon(
                        Icons.add,
                        color: AppTheme.textDarkColor,
                      ),
                      label: const Text('Cadastrar paciente'),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.tertiaryColor,
                        foregroundColor: AppTheme.textDarkColor,
                        padding: const EdgeInsets.symmetric(
                          horizontal: 20,
                          vertical: 16,
                        ),
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                    ),
                  ],
                );
              }
            },
          ),

          const SizedBox(height: 24),

          GridView.builder(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
              crossAxisCount: isDesktop ? 3 : 1,
              mainAxisExtent: 120,
              crossAxisSpacing: 16,
              mainAxisSpacing: 16,
            ),
            itemCount: mockPatients.length,
            itemBuilder: (context, index) {
              final patient = mockPatients[index];
              return UserCard(
                imagePath: patient['imagePath']!,
                name: patient['name']!,
                number: patient['number']!,
              );
            },
          ),
        ],
      ),
    );
  }
}
