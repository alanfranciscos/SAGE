import 'package:flutter/material.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';


class PatientDetails extends StatelessWidget {
  const PatientDetails({super.key});


  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: LayoutBuilder(
        builder: (context, constraints) {
          final bool isMobile = constraints.maxWidth < 800;


          return SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'Visualizar paciente',
                  style: TextStyle(
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                    color: AppTheme.textDarkColor,
                  ),
                ),
                const SizedBox(height: 16),
                if (isMobile) ..._buildMobilePatientInfo(),
                if (!isMobile)
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(
                        width: constraints.maxWidth * 0.5,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Dados do paciente',
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                                color: AppTheme.textDarkColor,
                              ),
                            ),
                            const SizedBox(height: 8),
                            _buildReadOnlyField('Nome', 'João da Silva'),
                            _buildReadOnlyField('CPF', '123.456.789-00'),
                            _buildReadOnlyField('Sexo', 'Masculino'),
                            _buildReadOnlyField('Data de Nascimento', '01/01/1990'),
                          ],
                        ),
                      ),
                      const SizedBox(width: 16),
                      Column(
                        children: [
                          const Text(
                            'Foto',
                            style: TextStyle(
                              fontWeight: FontWeight.bold,
                              fontSize: 16,
                              color: AppTheme.textDarkColor,
                            ),
                          ),
                          const SizedBox(height: 8),
                          _buildPhotoContainer(),
                        ],
                      ),
                    ],
                  ),
                const SizedBox(height: 16),


                if (isMobile)
                  ..._buildMobileEmergencyContactAndResidenceInfo(constraints),
                if (!isMobile)
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      SizedBox(
                        width: constraints.maxWidth * 0.5,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Dados do contato de emergência',
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                                color: AppTheme.textDarkColor,
                              ),
                            ),
                            const SizedBox(height: 8),
                            _buildReadOnlyField('Nome', 'Maria da Silva'),
                            _buildReadOnlyField('Telefone', '(31) 98765-4321'),
                            _buildReadOnlyField('Parentesco', 'Mãe'),
                          ],
                        ),
                      ),
                      const SizedBox(width: 16),
                      SizedBox(
                        width: constraints.maxWidth * 0.45,
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Dados da residência',
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                                color: AppTheme.textDarkColor,
                              ),
                            ),
                            const SizedBox(height: 8),
                            _buildReadOnlyField('Número do controle', '12345'),
                            _buildReadOnlyField('Número da casa', '25A'),
                          ],
                        ),
                      ),
                    ],
                  ),
                const SizedBox(height: 16),


                Row(
                  mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    TextButton(
                      onPressed: () {
                        context.go('/home');
                      },
                      style: TextButton.styleFrom(
                        foregroundColor: AppTheme.textDarkColor,
                        textStyle: TextStyle(fontWeight: FontWeight.bold),
                      ),
                      child: const Text('Voltar'),
                    ),
                    const SizedBox(width: 16),
                    ElevatedButton(
                      onPressed: () {
                        context.go('/patient-edit'); // Redireciona para a tela de edição
                      },
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.tertiaryColor,
                        foregroundColor: AppTheme.textDarkColor,
                      ),
                      child: const Text('Editar'),
                    ),
                  ],
                ),
              ],
            ),
          );
        },
      ),
    );
  }


  Widget _buildReadOnlyField(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: TextField(
        controller: TextEditingController(text: value),
        decoration: InputDecoration(
          labelText: label,
          filled: true,
          fillColor: Colors.grey[100],
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide.none,
          ),
          contentPadding: const EdgeInsets.symmetric(
            horizontal: 12,
            vertical: 8,
          ),
        ),
        enabled: false, // Desabilita a edição
      ),
    );
  }


  Widget _buildPhotoContainer() {
    return Container(
      height: 140,
      width: 140,
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.grey.shade400),
      ),
      child: const Center(child: Text('Foto do paciente')),
    );
  }


  List<Widget> _buildMobilePatientInfo() {
    return [
      const Text(
        'Dados do paciente',
        style: TextStyle(
          fontWeight: FontWeight.bold,
          fontSize: 16,
          color: AppTheme.textDarkColor,
        ),
      ),
      const SizedBox(height: 8),
      _buildReadOnlyField('Nome', 'João da Silva'),
      _buildReadOnlyField('CPF', '123.456.789-00'),
      _buildReadOnlyField('Sexo', 'Masculino'),
      _buildReadOnlyField('Data de Nascimento', '01/01/1990'),
      const SizedBox(height: 16),
      const Text(
        'Foto',
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
      ),
      const SizedBox(height: 8),
      _buildPhotoContainer(),
      const SizedBox(height: 16),
    ];
  }


  List<Widget> _buildMobileEmergencyContactAndResidenceInfo(
    BoxConstraints constraints,
  ) {
    return [
      const Text(
        'Dados do contato de emergência',
        style: TextStyle(
          fontWeight: FontWeight.bold,
          fontSize: 16,
          color: AppTheme.textDarkColor,
        ),
      ),
      const SizedBox(height: 8),
      _buildReadOnlyField('Nome', 'Maria da Silva'),
      _buildReadOnlyField('Telefone', '(31) 98765-4321'),
      _buildReadOnlyField('Parentesco', 'Mãe'),
      const SizedBox(height: 16),
      const Text(
        'Dados da residência',
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
      ),
      const SizedBox(height: 8),
      _buildReadOnlyField('Número do controle', '12345'),
      _buildReadOnlyField('Número da casa', '25A'),
      const SizedBox(height: 16),
    ];
  }
}



