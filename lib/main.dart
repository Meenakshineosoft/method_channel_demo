import 'dart:async';
import 'dart:core';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});


  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(

        primarySwatch: Colors.deepOrange,
      ),
      home: const MyHomePage(title: 'Method Channel Demo'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
 static const MethodChannel _channel = const MethodChannel('com.example.methodchannel/interop');
  late String _plaformMessage;

 static Future<String> get _platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<Null> get _number async {
   final int number = await _channel.invokeMethod('getNumber');
  }

  static Future<Null> _callMe() async {
   await _channel.invokeMethod('callMe');
  }

  static Future<dynamic> get _list async {
   final Map params = <String, dynamic> {
    'name': 'my name is Ram',
     'age': 28,
   };
   final List<dynamic> list = await _channel.invokeMethod('getlist', params);
   return list;
  }

  Future<dynamic> _platformCallHandler(MethodCall call) async{
   switch(call.method) {
     case 'callMe':
       print('call callMe : arguments = ${call.arguments}');
       return Future.value('called from platform !');
     default:
       print('unknown method : ${call.method}');
       throw MissingPluginException();
       break;
   }

}

 /* void _incrementCounter() {
    setState(() {

      _counter++;
    });
  } */

  @override
  void initState() {
    super.initState();
   _channel.setMethodCallHandler(_platformCallHandler);
   _platformVersion.then((value) => setState(() => _plaformMessage = value));
   _number.then((value) => null);
   _list.then((value) => print(value));
    _callMe();
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      appBar: AppBar(

        title: Text(widget.title),
      ),
      body: Center(

        child: Column(

          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
                'Your Platform : $_plaformMessage'
            ),
          ],
        ),
      ),
      );
  }
}
