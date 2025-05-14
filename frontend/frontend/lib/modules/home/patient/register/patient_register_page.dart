import 'package:flutter/material.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';
import 'register_controller.dart'; 

class PatientRegister extends StatefulWidget {
  const PatientRegister({super.key});

  @override
  State<PatientRegister> createState() => _PatientRegisterState();
}

class _PatientRegisterState extends State<PatientRegister> {
  final RegisterController controller = RegisterController();

  @override
  void dispose() {
    controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Cadastrar paciente')),
      body: SafeArea(
        child: LayoutBuilder(
          builder: (context, constraints) {
            final bool isMobile = constraints.maxWidth < 800;

            return SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
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
                              _buildSizedTextField(
                                'Nome',
                                300,
                                controller.nomeController,
                              ),
                              _buildSizedTextField(
                                'CPF',
                                180,
                                controller.cpfController,
                              ),
                              _buildSizedTextField(
                                'Sexo',
                                140,
                                controller.sexoController,
                              ),
                              _buildSizedTextField(
                                'Data de Nascimento',
                                200,
                                controller.dataNascimentoController,
                              ),
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
                    ..._buildMobileEmergencyContactAndResidenceInfo(
                      constraints,
                    ),
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
                              _buildSizedTextField(
                                'Nome',
                                300,
                                controller.contatoNomeController,
                              ),
                              _buildSizedTextField(
                                'Telefone',
                                180,
                                controller.contatoTelefoneController,
                              ),
                              _buildSizedTextField(
                                'Parentesco',
                                180,
                                controller.contatoParentescoController,
                              ),
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
                              _buildSizedTextField(
                                'Número do controle',
                                200,
                                controller.numeroControleController,
                              ),
                              _buildSizedTextField(
                                'Número da casa',
                                200,
                                controller.numeroCasaController,
                              ),
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
                          textStyle: const TextStyle(
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        child: const Text('Cancelar'),
                      ),
                      const SizedBox(width: 16),
                      ElevatedButton(
                        onPressed: () async {
                          final sucesso = await controller.enviarFormulario();
                          if (!mounted) return;

                          if (sucesso) {
                            context.go('/home');
                          } else {
                            // Exiba erro com context
                            ScaffoldMessenger.of(context).showSnackBar(
                              const SnackBar(
                                content: Text('Erro ao cadastrar paciente'),
                              ),
                            );
                          }
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: AppTheme.tertiaryColor,
                          foregroundColor: AppTheme.textDarkColor,
                        ),
                        child: const Text('Cadastrar paciente'),
                      ),
                    ],
                  ),
                ],
              ),
            );
          },
        ),
      ),
    );
  }

  Widget _buildTextField(String label, TextEditingController controller) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: TextField(
        controller: controller,
        decoration: InputDecoration(
          labelText: label,
          filled: true,
          fillColor: Colors.grey[100],
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: const BorderSide(width: 1),
          ),
          contentPadding: const EdgeInsets.symmetric(
            horizontal: 12,
            vertical: 8,
          ),
        ),
      ),
    );
  }

  Widget _buildSizedTextField(
    String label,
    double width,
    TextEditingController controller,
  ) {
    return SizedBox(width: width, child: _buildTextField(label, controller));
  }

  Widget _buildPhotoContainer() {
    return Container(
      height: 140,
      width: 140,
      decoration: BoxDecoration(
        color: Colors.grey[200],
        borderRadius: BorderRadius.circular(8),
        border: Border.all(),
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
      _buildSizedTextField('Nome', 300, controller.nomeController),
      _buildSizedTextField('CPF', 180, controller.cpfController),
      _buildSizedTextField('Sexo', 140, controller.sexoController),
      _buildSizedTextField(
        'Data de Nascimento',
        200,
        controller.dataNascimentoController,
      ),
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
      _buildSizedTextField('Nome', 300, controller.contatoNomeController),
      _buildSizedTextField(
        'Telefone',
        180,
        controller.contatoTelefoneController,
      ),
      _buildSizedTextField(
        'Parentesco',
        180,
        controller.contatoParentescoController,
      ),
      const SizedBox(height: 16),
      const Text(
        'Dados da residência',
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
      ),
      const SizedBox(height: 8),
      _buildSizedTextField(
        'Número do controle',
        200,
        controller.numeroControleController,
      ),
      _buildSizedTextField(
        'Número da casa',
        200,
        controller.numeroCasaController,
      ),
      const SizedBox(height: 16),
    ];
  }
}
