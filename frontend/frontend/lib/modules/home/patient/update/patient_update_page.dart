import 'package:flutter/material.dart';
import 'package:frontend/modules/home/patient/update/update_controller.dart';
import 'package:provider/provider.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';

class PatientUpdate extends StatefulWidget {
  final String id;

  const PatientUpdate({super.key, required this.id});

  @override
  _PatientUpdateState createState() => _PatientUpdateState();
}

class _PatientUpdateState extends State<PatientUpdate> {
  @override
  void initState() {
    super.initState();
    Future.microtask(
      () => Provider.of<PacienteUpdateController>(
        context,
        listen: false,
      ).fetchPacienteById(widget.id),
    );
  }

  @override
  Widget build(BuildContext context) {
    final pacienteController = Provider.of<PacienteUpdateController>(context);

    return Scaffold(
      appBar: AppBar(title: const Text('Editar dados do paciente')),
      body: SafeArea(
        child:
            pacienteController.isLoading
                ? const Center(child: CircularProgressIndicator())
                : pacienteController.hasError
                ? const Center(child: Text('Erro ao carregar os dados'))
                : LayoutBuilder(
                  builder: (context, constraints) {
                    final bool isMobile = constraints.maxWidth < 800;

                    return SingleChildScrollView(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const SizedBox(height: 16),
                          if (isMobile)
                            ..._buildMobilePatientInfo(pacienteController),
                          if (!isMobile)
                            Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: constraints.maxWidth * 0.5,
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
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
                                        pacienteController.nomeController,
                                      ),
                                      _buildSizedTextField(
                                        'CPF',
                                        180,
                                        pacienteController.cpfController,
                                      ),
                                      _buildSizedTextField(
                                        'Sexo',
                                        140,
                                        pacienteController.sexoController,
                                      ),
                                      _buildSizedTextField(
                                        'Data de Nascimento',
                                        200,
                                        pacienteController
                                            .dataNascimentoController,
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
                              pacienteController,
                            ),
                          if (!isMobile)
                            Row(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                SizedBox(
                                  width: constraints.maxWidth * 0.5,
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
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
                                        pacienteController
                                            .contatoNomeController,
                                      ),
                                      _buildSizedTextField(
                                        'Telefone',
                                        180,
                                        pacienteController
                                            .contatoTelefoneController,
                                      ),
                                      _buildSizedTextField(
                                        'Parentesco',
                                        180,
                                        pacienteController
                                            .contatoParentescoController,
                                      ),
                                    ],
                                  ),
                                ),
                                const SizedBox(width: 16),
                                SizedBox(
                                  width: constraints.maxWidth * 0.45,
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
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
                                        pacienteController
                                            .numeroControleController,
                                      ),
                                      _buildSizedTextField(
                                        'Número da casa',
                                        200,
                                        pacienteController.numeroCasaController,
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
                                ),
                                child: const Text('Cancelar'),
                              ),
                              const SizedBox(width: 16),
                              ElevatedButton(
                                onPressed: () {
                                  pacienteController.updatePaciente(widget.id);
                                  context.go('/home');
                                },
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: AppTheme.tertiaryColor,
                                  foregroundColor: AppTheme.textDarkColor,
                                ),
                                child: const Text('Salvar alterações'),
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

  // Widget _buildSizedTextField(
  //   String label,
  //   TextEditingController controller,
  //   double width,
  // ) {
  //   return Padding(
  //     padding: const EdgeInsets.only(bottom: 10),
  //     child: TextField(
  //       controller: controller,
  //       decoration: InputDecoration(
  //         labelText: label,
  //         filled: true,
  //         fillColor: Colors.grey[100],
  //         border: OutlineInputBorder(
  //           borderRadius: BorderRadius.circular(8),
  //           borderSide: const BorderSide(width: 1),
  //         ),
  //         contentPadding: const EdgeInsets.symmetric(
  //           horizontal: 12,
  //           vertical: 8,
  //         ),
  //       ),
  //     ),
  //   );
  // }

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

  List<Widget> _buildMobilePatientInfo(
    PacienteUpdateController pacienteController,
  ) {
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
      _buildSizedTextField('Nome', 300, pacienteController.nomeController),
      _buildSizedTextField('CPF', 180,pacienteController.cpfController),
      _buildSizedTextField('Sexo',140, pacienteController.sexoController ),
      _buildSizedTextField(
        'Data de Nascimento',
        200,
        pacienteController.dataNascimentoController,
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
    PacienteUpdateController pacienteController,
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
      _buildSizedTextField(
        'Nome',
        300,
        pacienteController.contatoNomeController,
      ),
      _buildSizedTextField(
        'Telefone',
        180,
        pacienteController.contatoTelefoneController,
      ),
      _buildSizedTextField(
        'Parentesco',
        180,
        pacienteController.contatoParentescoController,
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
        pacienteController.numeroControleController,
      ),
      _buildSizedTextField(
        'Número da casa',
        200,
        pacienteController.numeroCasaController,
      ),
      const SizedBox(height: 16),
    ];
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
}
