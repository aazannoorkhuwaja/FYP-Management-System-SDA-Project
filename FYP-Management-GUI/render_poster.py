"""
Canvas Design: Monolithic Serenity — FYP Management Poster
Expressed in pure Pillow. Museum-quality, brutalist-refined.
"""

import math
import random
from PIL import Image, ImageDraw, ImageFont, ImageFilter

# ── Canvas dimensions (A3-ish @150dpi) ─────────────────────────────────────
W, H = 2480, 3508   # Portrait A3

# ── Palette ─────────────────────────────────────────────────────────────────
BG          = (10, 10, 12)          # near-black
DARK_SLAB   = (18, 20, 26)          # lifted dark
MID_GREY    = (38, 40, 50)
LIGHT_GREY  = (90, 95, 108)
GHOST       = (160, 165, 178)
OFF_WHITE   = (230, 232, 236)
ACCENT_1    = (220, 80, 40)         # vermillion
ACCENT_2    = (40, 140, 180)        # steel-blue
WHITE       = (255, 255, 255)

random.seed(2025)

img  = Image.new("RGB", (W, H), BG)
draw = ImageDraw.Draw(img)

# ── Helper: find a usable monospace font ────────────────────────────────────
FONT_PATHS = [
    "/usr/share/fonts/truetype/dejavu/DejaVuSansMono.ttf",
    "/usr/share/fonts/truetype/liberation/LiberationMono-Regular.ttf",
    "/usr/share/fonts/truetype/freefont/FreeMono.ttf",
    "/usr/share/fonts/truetype/ubuntu/UbuntuMono-R.ttf",
]
SERIF_PATHS = [
    "/usr/share/fonts/truetype/dejavu/DejaVuSerif.ttf",
    "/usr/share/fonts/truetype/liberation/LiberationSerif-Regular.ttf",
    "/usr/share/fonts/truetype/freefont/FreeSerif.ttf",
]
BOLD_PATHS = [
    "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf",
    "/usr/share/fonts/truetype/liberation/LiberationSans-Bold.ttf",
    "/usr/share/fonts/truetype/freefont/FreeSansBold.ttf",
]
SANS_PATHS = [
    "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
    "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf",
    "/usr/share/fonts/truetype/freefont/FreeSans.ttf",
]

def load_font(paths, size):
    for p in paths:
        try:
            return ImageFont.truetype(p, size)
        except (IOError, OSError):
            pass
    return ImageFont.load_default()

fnt_micro  = load_font(FONT_PATHS,  28)
fnt_small  = load_font(FONT_PATHS,  44)
fnt_label  = load_font(SANS_PATHS,  56)
fnt_body   = load_font(SANS_PATHS,  72)
fnt_bold   = load_font(BOLD_PATHS, 120)
fnt_display= load_font(BOLD_PATHS, 210)
fnt_super  = load_font(BOLD_PATHS, 340)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 1 — Tiling grid of hairline squares in the lower half (background texture)
# ════════════════════════════════════════════════════════════════════════════
CELL = 80
for gy in range(0, H, CELL):
    for gx in range(0, W, CELL):
        alpha = 30 if gy > H // 2 else 12
        c = tuple(min(255, BG[i] + alpha) for i in range(3))
        draw.rectangle([gx, gy, gx + CELL - 1, gy + CELL - 1], outline=c, width=1)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 2 — Massive dark slab (left column, full height)
# ════════════════════════════════════════════════════════════════════════════
SLAB_W = 420
draw.rectangle([0, 0, SLAB_W, H], fill=DARK_SLAB)

# Vertical rule inside slab
draw.line([(SLAB_W - 2, 0), (SLAB_W - 2, H)], fill=MID_GREY, width=2)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 3 — Accent color bars (horizontal, upper register)
# ════════════════════════════════════════════════════════════════════════════
# Thick vermillion stripe
draw.rectangle([SLAB_W, 0, W, 18], fill=ACCENT_1)
# Steel-blue thin rule just below
draw.rectangle([SLAB_W, 18, W, 30], fill=ACCENT_2)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 4 — Rotated text in the slab (vertical label)
# ════════════════════════════════════════════════════════════════════════════
vert_label = "FYP MANAGEMENT SYSTEM  ·  SDA ASSIGNMENT-3"
tmp_w = len(vert_label) * 34 + 100
tmp = Image.new("RGBA", (tmp_w, 200), (0, 0, 0, 0))
tdraw = ImageDraw.Draw(tmp)
tdraw.text((10, 60), vert_label, font=fnt_small, fill=GHOST)
tmp_rot = tmp.rotate(90, expand=True)
paste_y = (H - tmp_rot.height) // 2
img.paste(tmp_rot, (28, paste_y), tmp_rot)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 5 — Hero monogram block (top-left of canvas area)
# ════════════════════════════════════════════════════════════════════════════
MARGIN_L = SLAB_W + 90
TOP_Y    = 80

# Giant "FYP" — clipped/bleeds slightly
draw.text((MARGIN_L, TOP_Y - 30), "FYP", font=fnt_super, fill=OFF_WHITE)

# Accent line below hero text
hero_bottom = TOP_Y + 320
draw.rectangle([MARGIN_L, hero_bottom, W - 80, hero_bottom + 6], fill=ACCENT_1)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 6 — Sub-heading block
# ════════════════════════════════════════════════════════════════════════════
SH_Y = hero_bottom + 40
draw.text((MARGIN_L, SH_Y), "MANAGEMENT", font=fnt_display, fill=WHITE)
draw.text((MARGIN_L, SH_Y + 225), "PLATFORM", font=fnt_display, fill=ACCENT_2)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 7 — Data/info section (mid-canvas, right offset)
# ════════════════════════════════════════════════════════════════════════════
INFO_Y = SH_Y + 500
INFO_L = MARGIN_L

# Horizontal rule
draw.rectangle([INFO_L, INFO_Y, W - 80, INFO_Y + 2], fill=MID_GREY)
INFO_Y += 30

# Three-column micro-labels
cols = [
    ("ROLE",      "Supervisor · Student · Committee"),
    ("MODULE",    "Peer Review · Rubric · Progress"),
    ("STACK",     "Java Swing · MVC · OOP · SDA"),
]
cx = INFO_L
col_w = (W - 80 - INFO_L) // 3
for title, sub in cols:
    draw.text((cx, INFO_Y),       title, font=fnt_small,  fill=ACCENT_1)
    draw.text((cx, INFO_Y + 55),  sub,   font=fnt_micro,  fill=GHOST)
    cx += col_w

# ════════════════════════════════════════════════════════════════════════════
# LAYER 8 — Central diagram: abstract architecture glyph
# ════════════════════════════════════════════════════════════════════════════
# Represents system nodes — circular nodes connected by thin lines
DIAG_CX = W // 2 + 80
DIAG_CY = H // 2 + 80
DIAG_R  = 340

nodes = []
roles  = ["Supervisor", "Student", "Committee", "System", "FYP\nCoord"]
angles = [90, 210, 330, 0, 180]

for i, (label, angle) in enumerate(zip(roles, angles)):
    rad   = math.radians(angle)
    r     = DIAG_R if i < 3 else DIAG_R * 0.52
    nx    = int(DIAG_CX + r * math.cos(rad))
    ny    = int(DIAG_CY - r * math.sin(rad))
    nodes.append((nx, ny, label))

# Draw connector lines first
for i, (nx, ny, _) in enumerate(nodes):
    for j, (mx, my, _) in enumerate(nodes):
        if i >= j:
            continue
        draw.line([(nx, ny), (mx, my)], fill=MID_GREY, width=2)

# Draw nodes
NR = 72
for nx, ny, lbl in nodes:
    # outer ring
    draw.ellipse([nx - NR, ny - NR, nx + NR, ny + NR], outline=ACCENT_2, width=3)
    # fill
    draw.ellipse([nx - NR + 4, ny - NR + 4, nx + NR - 4, ny + NR - 4], fill=DARK_SLAB)
    # label inside
    lines = lbl.split("\n")
    offset_y = ny - (len(lines) * 20)
    for line in lines:
        bbox = draw.textbbox((0, 0), line, font=fnt_micro)
        tw = bbox[2] - bbox[0]
        draw.text((nx - tw // 2, offset_y), line, font=fnt_micro, fill=OFF_WHITE)
        offset_y += 32

# Central hub marker
draw.ellipse([DIAG_CX - 22, DIAG_CY - 22, DIAG_CX + 22, DIAG_CY + 22], fill=ACCENT_1)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 9 — Scattered dot-field (noise / texture, lower region)
# ════════════════════════════════════════════════════════════════════════════
for _ in range(2200):
    dx = random.randint(SLAB_W + 20, W - 20)
    dy = random.randint(H // 2, H - 20)
    r  = random.randint(1, 3)
    brightness = random.randint(30, 60)
    col = (brightness, brightness + 5, brightness + 10)
    draw.ellipse([dx - r, dy - r, dx + r, dy + r], fill=col)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 10 — Feature modules list (lower left of canvas area)
# ════════════════════════════════════════════════════════════════════════════
FEAT_Y = H - 700
FEAT_L = MARGIN_L

features = [
    "01  Project Proposal & Registration",
    "02  Supervisor / Student Dashboard",
    "03  Milestone Progress Tracking",
    "04  Peer Review & Rubric Scoring",
    "05  Committee Defence Evaluation",
    "06  Individual Contribution Logging",
]

draw.rectangle([FEAT_L, FEAT_Y - 16, FEAT_L + 4, FEAT_Y + (len(features) * 70)], fill=ACCENT_1)

for i, feat in enumerate(features):
    fy = FEAT_Y + i * 70
    draw.text((FEAT_L + 28, fy), feat, font=fnt_label, fill=GHOST if i % 2 == 0 else OFF_WHITE)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 11 — Footer rule + metadata
# ════════════════════════════════════════════════════════════════════════════
FOOT_Y = H - 80
draw.rectangle([SLAB_W, FOOT_Y, W, FOOT_Y + 2], fill=MID_GREY)
draw.text((MARGIN_L, FOOT_Y + 12),
          "NATIONAL UNIVERSITY OF COMPUTER & EMERGING SCIENCES  ·  SOFTWARE DESIGN & ARCHITECTURE  ·  ASSIGNMENT 3",
          font=fnt_micro, fill=LIGHT_GREY)

draw.text((W - 260, FOOT_Y + 12), "2025–26", font=fnt_micro, fill=ACCENT_1)

# ════════════════════════════════════════════════════════════════════════════
# LAYER 12 — Coordinate markers (top-right corner, Brutalist reference grid)
# ════════════════════════════════════════════════════════════════════════════
for i, (cx2, cy2) in enumerate([(W - 200, 60), (W - 120, 60), (W - 200, 130), (W - 120, 130)]):
    draw.ellipse([cx2 - 6, cy2 - 6, cx2 + 6, cy2 + 6],
                 fill=ACCENT_2 if i % 2 == 0 else ACCENT_1)
draw.text((W - 240, 150), "REF·A3·001", font=fnt_micro, fill=LIGHT_GREY)

# ── Subtle vignette ──────────────────────────────────────────────────────────
vignette = Image.new("L", (W, H), 255)   # start fully white
vd = ImageDraw.Draw(vignette)
STEPS = 140
half_w, half_h = W // 2, H // 2
for s in range(STEPS):
    margin = s * (min(half_w, half_h) // STEPS)
    x0, y0 = margin, margin
    x1, y1 = W - margin, H - margin
    if x1 <= x0 or y1 <= y0:
        break
    bright = int(255 * (s / STEPS) ** 0.6)
    vd.rectangle([x0, y0, x1, y1], outline=bright)

vignette = vignette.filter(ImageFilter.GaussianBlur(radius=90))
# Use vignette as alpha mask to blend img toward black at edges
black = Image.new("RGB", (W, H), (0, 0, 0))
img_out = Image.composite(img, black, vignette)

# ── Save ─────────────────────────────────────────────────────────────────────
out_path = "./fyp_management_poster.png"
img_out.save(out_path, "PNG", dpi=(150, 150))
print(f"Saved → {out_path}")
