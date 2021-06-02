
import 'dart:async';

import 'package:flutter/services.dart';

class PackageChannel {
  static const MethodChannel _channel =
      const MethodChannel('package_channel');

  static Future<String> get channel async {
    final String version = await _channel.invokeMethod('getChannel');
    return version;
  }
}
