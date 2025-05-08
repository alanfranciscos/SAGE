import 'package:flutter/material.dart';
import 'widgets/sidebar.dart';

class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    final bool isDesktop = MediaQuery.of(context).size.width >= 800;

    return Scaffold(
      appBar: isDesktop ? null : AppBar(title: const Text('Home')),
      drawer: isDesktop ? null : const Drawer(child: Sidebar()),
      body: Row(
        children: [
          if (isDesktop) const SizedBox(width: 200, child: Sidebar()),
          const Expanded(child: Center(child: Text('Página Inicial'))),
        ],
      ),
    );
  }
}
