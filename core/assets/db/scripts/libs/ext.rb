def puts(string)
  super(string)
  ForgE.log("<console>", string.to_s)
end

def exit
  Gdx.app.exit
end
