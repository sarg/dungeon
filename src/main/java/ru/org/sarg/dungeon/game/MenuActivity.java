package ru.org.sarg.dungeon.game;

import ru.org.sarg.dungeon.Dungeon;
import ru.org.sarg.dungeon.render.IDisplay;
import ru.org.sarg.dungeon.window.MenuWindow;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenuActivity extends Activity {
    MenuWindow menuWindow;
    LoadActivity loadActivity;

    public MenuActivity(IDisplay display) {
        super(display);
    }

    @Override
    public void onKeyDown(int key) {
        if (isLoading())
            loadActivity.onKeyDown(key);
        else
            menuWindow.onKeyDown(key);
    }

    @Override
    public void start() {
        List<Menu.Option> choices = Arrays.asList(
                new Menu.Option("New", () -> Dungeon.INSTANCE.setActivity(new CharacterCreateActivity(display))),
                new Menu.Option("Load", () -> setLoading(true)),
                new Menu.Option("Quit", () -> Dungeon.INSTANCE.quit())
        );

        menuWindow = new MenuWindow(new Menu(choices));
    }

    private boolean isLoading() {
        return loadActivity != null;
    }

    private void setLoading(boolean l) {
        assert (isLoading() != l);

        if (l) {
            loadActivity = new LoadActivity(display);
            loadActivity.start();
        } else {
            loadActivity = null;
        }
    }

    @Override
    public void draw() {
        if (isLoading())
            loadActivity.draw();
        else
            menuWindow.draw(display);
    }

    public class LoadActivity extends Activity {
        private MenuWindow loadMenuWindow;

        public LoadActivity(IDisplay display) {
            super(display);
        }

        @Override
        public void onKeyDown(int key) {
            loadMenuWindow.onKeyDown(key);
        }

        @Override
        public void start() {
            Path cwd = FileSystems.getDefault().getPath("");
            List<Menu.Option> choices;
            try {
                choices = Files.find(cwd, 1, (path, attr) -> String.valueOf(path).endsWith(".sav"))
                        .map(f -> new Menu.Option(f.getFileName().toString(), () -> GameActivity.INSTANCE.loadSaveFile(f)))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Menu menu;
            if (choices.isEmpty())
                menu = new Menu(Arrays.asList(new Menu.Option("NO SAVES", () -> setLoading(false))));
            else
                menu = new Menu(choices);

            loadMenuWindow = new MenuWindow(menu);
        }

        @Override
        public void draw() {
            loadMenuWindow.draw(display);
        }
    }
}
