def get_highest_build_number(
    bundle_identifier:,
    package_name:,
    google_play_json_key_path:,
    firebase_json_key_path:,
    firebase_app_ios:,
    firebase_app_android:
  )
  app_store_build_number = latest_testflight_build_number(
    app_identifier: bundle_identifier,
  )
  
  puts("build number (App Store): #{app_store_build_number}")

  google_play_build_number = google_play_track_version_codes(
    package_name: package_name,
    json_key: google_play_json_key_path,
  ).max

  puts("build number (Google Play Store): #{google_play_build_number}")

  fad_build_number_ios = firebase_app_distribution_get_latest_release(
    app: firebase_app_ios,
    service_credentials_file: firebase_json_key_path,
  )[:buildVersion].to_i

  fad_build_number_android = firebase_app_distribution_get_latest_release(
    app: firebase_app_android,
    service_credentials_file: firebase_json_key_path,
  )[:buildVersion].to_i

  puts("build number (Firebase App Distribution iOS): #{fad_build_number_ios}")
  puts("build number (Firebase App Distribution Android): #{fad_build_number_android}")

  return [
    app_store_build_number,
    google_play_build_number,
    fad_build_number_ios,
    fad_build_number_android,
  ].max
end
