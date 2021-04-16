package hr.fer.zemris.lystems.impl.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lystems.impl.LSystemBuilderImpl;

public class Glavni3 {
    public static void main(String... args) {
        LSystemViewer.showLSystem(LSystemBuilderImpl::new);
    }
}
