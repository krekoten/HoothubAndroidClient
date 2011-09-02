begin
  require 'bundler/setup'
  Bundler.require
rescue LoadError
  abort "Install bundler `gem install bundle`"
rescue Bundler::GemNotFound
  abort "Run `bundle install`"
end

require 'sinatra'
require 'json'

get '/' do
  params.empty? ? 'Empty' : params.to_json
end