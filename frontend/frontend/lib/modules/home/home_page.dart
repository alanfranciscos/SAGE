import 'package:flutter/material.dart';
import 'widgets/sidebar.dart';

class HomePage extends StatelessWidget {
  final Widget child;

  const HomePage({super.key, required this.child});

  @override
  Widget build(BuildContext context) {
    final bool isDesktop = MediaQuery.of(context).size.width >= 800;

    return Scaffold(
      appBar: isDesktop ? null : AppBar(title: const Text('Home')),
      drawer: isDesktop ? null : const Drawer(child: Sidebar()),
      body: Row(
        children: [
          if (isDesktop) const SizedBox(width: 200, child: Sidebar()),

          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: SingleChildScrollView(child: child),
            ),
          ),
        ],
      ),
    );
  }
}
