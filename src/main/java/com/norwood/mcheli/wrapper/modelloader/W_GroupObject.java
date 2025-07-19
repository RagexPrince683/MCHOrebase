package com.norwood.mcheli.wrapper.modelloader;

import com.google.common.base.Joiner;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class W_GroupObject {
    public final String name;
    public final ArrayList<W_Face> faces = new ArrayList<>();
    public int glDrawingMode;

    public W_GroupObject() {
        this("");
    }

    public W_GroupObject(String name) {
        this(name, -1);
    }

    public W_GroupObject(String name, int glDrawingMode) {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    public void render() {
        if (!this.faces.isEmpty()) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            builder.begin(this.glDrawingMode, DefaultVertexFormats.POSITION_TEX_NORMAL);
            this.render(tessellator);
            tessellator.draw();
        }
    }

    public void render(Tessellator tessellator) {
        if (!this.faces.isEmpty()) {
            for (W_Face face : this.faces) {
                face.addFaceForRender(tessellator);
            }
        }
    }

    @Override
    public String toString() {
        return "W_GroupObject[size=" + this.faces.size() + ",values=[" + Joiner.on('\n').join(this.faces) + "]]";
    }
}
