// class PatientModel {
//   final String id;
//   final String nome;
//   final String cpf;
//   final String sexo;
//   final String dataNascimento;
//   final String contatoNome;
//   final String contatoTelefone;
//   final String contatoParentesco;
//   final String numeroControle;
//   final String numeroCasa;

//   PatientModel({
//     required this.id,
//     required this.nome,
//     required this.cpf,
//     required this.sexo,
//     required this.dataNascimento,
//     required this.contatoNome,
//     required this.contatoTelefone,
//     required this.contatoParentesco,
//     required this.numeroControle,
//     required this.numeroCasa,
//   });

//   factory PatientModel.fromJson(Map<String, dynamic> json) {
//     return PatientModel(
//       id: json['id'],
//       nome: json['paciente']['dadosPessoais']['nome'],
//       cpf: json['paciente']['dadosPessoais']['cpf'],
//       sexo: json['paciente']['dadosPessoais']['sexo'],
//       dataNascimento: json['paciente']['dadosPessoais']['dataNascimento'],
//       contatoNome: json['paciente']['contatoEmergencia']['nome'],
//       contatoTelefone: json['paciente']['contatoEmergencia']['telefone'],
//       contatoParentesco: json['paciente']['contatoEmergencia']['parentesco'],
//       numeroControle: json['paciente']['residencia']['numeroControle'],
//       numeroCasa: json['paciente']['residencia']['numeroCasa'],
//     );
//   }

//   get status => null;
// }

class PatientModel {
  final String id;
  final String nome;
  final String cpf;
  final String sexo;
  final String dataNascimento;
  final String contatoNome;
  final String contatoTelefone;
  final String contatoParentesco;
  final String numeroControle;
  final String numeroCasa;
  final String status;

  PatientModel({
    required this.id,
    required this.nome,
    required this.cpf,
    required this.sexo,
    required this.dataNascimento,
    required this.contatoNome,
    required this.contatoTelefone,
    required this.contatoParentesco,
    required this.numeroControle,
    required this.numeroCasa,
    required this.status,
  });

  factory PatientModel.fromJson(Map<String, dynamic> json) {
    return PatientModel(
      id: json['id'] ?? '',
      nome: json['paciente']['dadosPessoais']['nome'] ?? 'Sem nome',
      cpf: json['paciente']['dadosPessoais']['cpf'] ?? '',
      sexo: json['paciente']['dadosPessoais']['sexo'] ?? '',
      dataNascimento: json['paciente']['dadosPessoais']['dataNascimento'] ?? '',
      contatoNome: json['paciente']['contatoEmergencia']['nome'] ?? '',
      contatoTelefone: json['paciente']['contatoEmergencia']['telefone'] ?? '',
      contatoParentesco: json['paciente']['contatoEmergencia']['parentesco'] ?? '',
      numeroControle: json['paciente']['residencia']['numeroControle'] ?? '',
      numeroCasa: json['paciente']['residencia']['numeroCasa'] ?? '',
      status: json['paciente']['status'] ?? '0',
    );
  }
}
