import 'package:flutter/material.dart';
import 'package:frontend/modules/home/patient/details/details_controller.dart';
import 'package:provider/provider.dart';
import 'package:frontend/shared/themes/app_theme.dart';
import 'package:go_router/go_router.dart';

class PatientDetails extends StatefulWidget {
  final String id;

  const PatientDetails({super.key, required this.id});

  @override
  _PatientDetailsState createState() => _PatientDetailsState();
}

class _PatientDetailsState extends State<PatientDetails> {
  List<Widget> _buildMobilePatientInfo(
    PacienteDetailsController pacienteController,
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
      _buildSizedReadOnlyField('Nome', pacienteController.paciente!.nome, 300),
      _buildSizedReadOnlyField('CPF', pacienteController.paciente!.cpf, 180),
      _buildSizedReadOnlyField('Sexo', pacienteController.paciente!.sexo, 140),
      _buildSizedReadOnlyField(
        'Data de Nascimento',
        pacienteController.paciente!.dataNascimento,
        200,
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
    PacienteDetailsController pacienteController,
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
      _buildSizedReadOnlyField(
        'Nome',
        pacienteController.paciente!.contatoNome,
        300,
      ),
      _buildSizedReadOnlyField(
        'Telefone',
        pacienteController.paciente!.contatoTelefone,
        180,
      ),
      _buildSizedReadOnlyField(
        'Parentesco',
        pacienteController.paciente!.contatoParentesco,
        180,
      ),
      const SizedBox(height: 16),
      const Text(
        'Dados da residência',
        style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
      ),
      const SizedBox(height: 8),
      _buildSizedReadOnlyField(
        'Número do controle',
        pacienteController.paciente!.numeroControle,
        200,
      ),
      _buildSizedReadOnlyField(
        'Número da casa',
        pacienteController.paciente!.numeroCasa,
        200,
      ),
      const SizedBox(height: 16),
    ];
  }

  @override
  void initState() {
    super.initState();
    Future.microtask(
      () => Provider.of<PacienteDetailsController>(
        context,
        listen: false,
      ).fetchPacienteById(widget.id),
    );
  }

  @override
  Widget build(BuildContext context) {
    final pacienteController = Provider.of<PacienteDetailsController>(context);

    return Scaffold(
      appBar: AppBar(title: const Text('Visualizar paciente')),
      body: SafeArea(
        child:
            pacienteController.isLoading
                ? const Center(
                  child: CircularProgressIndicator(),
                ) // Exibe loading enquanto busca os dados
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
                                      _buildSizedReadOnlyField(
                                        'Nome',
                                        pacienteController.paciente!.nome,
                                        300,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'CPF',
                                        pacienteController.paciente!.cpf,
                                        180,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'Sexo',
                                        pacienteController.paciente!.sexo,
                                        140,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'Data de Nascimento',
                                        pacienteController
                                            .paciente!
                                            .dataNascimento,
                                        200,
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
                                      _buildSizedReadOnlyField(
                                        'Nome',
                                        pacienteController
                                            .paciente!
                                            .contatoNome,
                                        300,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'Telefone',
                                        pacienteController
                                            .paciente!
                                            .contatoTelefone,
                                        180,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'Parentesco',
                                        pacienteController
                                            .paciente!
                                            .contatoParentesco,
                                        180,
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
                                      _buildSizedReadOnlyField(
                                        'Número do controle',
                                        pacienteController
                                            .paciente!
                                            .numeroControle,
                                        200,
                                      ),
                                      _buildSizedReadOnlyField(
                                        'Número da casa',
                                        pacienteController.paciente!.numeroCasa,
                                        200,
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
                                child: const Text('Voltar'),
                              ),
                              const SizedBox(width: 16),
                              ElevatedButton(
                                onPressed: () {
                                  context.go('/patient-update/${widget.id}');
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
      ),
    );
  }

  Widget _buildReadOnlyField(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: TextField(
        readOnly: true,
        controller: TextEditingController(text: value),
        decoration: InputDecoration(
          labelText: label,
          filled: true,
          fillColor: Colors.grey[200],
          border: OutlineInputBorder(
            borderRadius: BorderRadius.circular(8),
            borderSide: BorderSide.none,
          ),
          contentPadding: const EdgeInsets.symmetric(
            horizontal: 12,
            vertical: 8,
          ),
        ),
      ),
    );
  }

  Widget _buildSizedReadOnlyField(String label, String value, double width) {
    return SizedBox(width: width, child: _buildReadOnlyField(label, value));
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
}
