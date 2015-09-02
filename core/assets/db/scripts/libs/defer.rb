module Defer
  class Callback
    include Runnable

    def initialize(callback, result)
      @callback = callback
      @result   = result
    end

    def run
      @callback.call(@result)
      @callback = nil
      @result   = nil
    end
  end

  def self.main(&callback)
    Gdx.app.postRunnable(Defer::Callback.new(callback, e))
  end

  def self.exec(operation, &callback)
    Thread.new do
      begin
        result = operation.call
        Gdx.app.postRunnable(Defer::Callback.new(callback, result))
      rescue Exception => e
        Gdx.app.postRunnable(Defer::Callback.new(callback, e))
      end
    end
  end
end
