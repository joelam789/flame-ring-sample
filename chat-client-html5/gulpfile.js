

const del = require('del');
const gulp = require('gulp');
const sequence = require('run-sequence');
const sourcemap = require('gulp-sourcemaps');
const webserver = require('gulp-webserver');

const tsconfig = require('gulp-typescript').createProject('tsconfig.json');

gulp.task('clean', function () {
    del.sync(["./dist/**/*"]);
});

gulp.task('copy-lib', function () {
    return gulp.src([
        "./jspm_packages/**/*"
        ])
        .pipe(gulp.dest("./dist/jspm_packages/"));
});

gulp.task('copy-index', function () {
    return gulp.src([
        "./index.html",
        "./config.js"
        ])
        .pipe(gulp.dest("./dist/"));
});

gulp.task('copy-template', function () {
    return gulp.src([
        "./src/*.html",
        "./src/*.css"
        ])
        .pipe(gulp.dest("./dist/"));
});

gulp.task('copy-resource', function () {
    return gulp.src([
        "./res/**/*"
        ])
        .pipe(gulp.dest("./dist/"));
});

gulp.task("build", () => {
    return gulp.src([
        "./typings/index.d.ts",
        "./src/*.ts"
    ])
    .pipe(sourcemap.init({ loadMaps: true }))
    .pipe(tsconfig()).js
    .pipe(sourcemap.write("./", {includeContent: false, sourceRoot: '../src'}))
    .pipe(gulp.dest("./dist/"));
});

gulp.task("watch", function () {
    return gulp.watch(["./index.html", "./src/*.*", "./res/**/*"],
                      ["copy-index", "copy-template", "copy-resource", "build"]);
});

gulp.task("rebuild", function () {
    sequence('clean',
            ['copy-lib', 'copy-index'],
             'copy-template',
             'copy-resource',
             'build',
             'watch');
});

gulp.task("build-only", function () {
    sequence('clean',
            ['copy-lib', 'copy-index'],
             'copy-template',
             'copy-resource',
             'build');
});

gulp.task('start', function() {
  gulp.src('./')
  .pipe(webserver({
      port: 9090
    }));
});

gulp.task('default', ['rebuild']);
