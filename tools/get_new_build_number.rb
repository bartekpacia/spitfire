require_relative "./get_highest_build_number.rb"

def get_new_build_number(
    package_name:,
    google_play_json_key_path:
  )
  puts "pwd: #{Dir.pwd}"

  file = "../../new_build_number.txt"
  if(File.exist?(file)) 
    puts "file exists - reading!" 
    highest_build_number = File.read(file).to_i
  else 
    puts "file not found - getting highest build number"
    highest_build_number = get_highest_build_number(
      package_name: package_name,
      google_play_json_key_path: google_play_json_key_path,
    )
    
    File.open(file, "w") { |f| f.write "#{highest_build_number}\n" }
  end

  puts "highest_build_number: #{highest_build_number}"

  new_highest_build_number = highest_build_number + 1
  puts "new_highest_build_number: #{new_highest_build_number}"

  return new_highest_build_number
end
