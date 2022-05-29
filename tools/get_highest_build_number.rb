def get_highest_build_number(
    package_name:,
    google_play_json_key_path:
  )
  google_play_build_number = google_play_track_version_codes(
    package_name: package_name,
    json_key: google_play_json_key_path,
  ).max

  puts("build number (Google Play Store): #{google_play_build_number}")

  return [google_play_build_number].max
end
