module UI
  class Button < com.badlogic.gdx.scenes.scene2d.ui.TextButton
    def initialize(text, style=nil)
      super(text, ForgE.ui.skin, style || "default")
      @callbacks = []
    end

    def onClick(&block)
      callback = ClickCallback.new
      callback.block = block
      @callbacks << callback
      addListener(callback)
    end

    def dispose
      @callbacks.each { |callback| removeListener(callback) }
      @callbacks.clear
    end
  end
end
