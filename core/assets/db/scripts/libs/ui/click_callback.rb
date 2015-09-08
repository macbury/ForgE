module UI
  class ClickCallback < com.badlogic.gdx.scenes.scene2d.utils.ClickListener
    attr_accessor :block

    def clicked(event, x,y)
      block.call
    end
  end
end
