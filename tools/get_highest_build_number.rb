def get_highest_build_number(
    package_name:,
    google_play_json_key_path:
  )
  google_play_build_number_prod = get_version_code(
    track: "production",
    package_name: package_name,
    json_key: google_play_json_key_path,
  )

  google_play_build_number_beta = get_version_code(
    track: "beta",
    package_name: package_name,
    json_key: google_play_json_key_path,
  )

  google_play_build_number_alpha = get_version_code(
    track: "alpha",
    package_name: package_name,
    json_key: google_play_json_key_path,
  )

  google_play_build_number_internal = get_version_code(
    track: "internal",
    package_name: package_name,
    json_key: google_play_json_key_path,
  )

  google_play_build_number = [
    google_play_build_number_prod,
    google_play_build_number_beta,
    google_play_build_number_alpha,
    google_play_build_number_internal,
  ].max

  puts("build number (Google Play Store): #{google_play_build_number}")

  return [google_play_build_number].max
end

def get_version_code(track:, package_name:, json_key:)
  begin
    codes = google_play_track_version_codes(
      track: track,
      package_name: package_name,
      json_key: json_key,
    )

    return codes.max
  rescue
    puts "Version code not found for track #{track}"
    return 0
  end
end
