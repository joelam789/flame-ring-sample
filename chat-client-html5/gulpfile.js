

const del = require('del');
const gulp = require('gulp');
const sequence = require('run-sequence');
const sourcemap = require('gulp-sourcemaps');
const webserver = require('gulp-webserver');

const fs = require("fs");
const abundle = require('aurelia-bundler').bundle;

const tsconfig = require('gulp-typescript').createProject('tsconfig.json');

gulp.task('clear-all', function () {
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
        "./index.html"
        ])
        .pipe(gulp.dest("./dist/"));
});

gulp.task('copy-config', function () {
    return gulp.src(["./config.js"]).pipe(gulp.dest("./dist/"));
});

gulp.task('backup-config', function () {
    return gulp.src(["./config.js"]).pipe(gulp.dest("./tmp/"));
});

gulp.task('restore-config', function () {
    return gulp.src(["./tmp/config.js"]).pipe(gulp.dest("./"));
});

gulp.task('copy-bundle', function () {
    return gulp.src([
        "./bundle-app.js",
        "./bundle-vendor.js"
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
    sequence('clear-all',
            ['copy-lib', 'copy-index', 'copy-config', 'copy-bundle'],
             'copy-template',
             'copy-resource',
             'build',
             'watch');
});

gulp.task("bundle", function () {
    del.sync(["./dist/bundle-app.js"]);
    del.sync(["./dist/bundle-vendor.js"]);
    return abundle(JSON.parse(fs.readFileSync('./bundle.json', 'utf8')));
});

gulp.task('clean-up', function () {
    del.sync(["dist/*.js.map"]);
    del.sync(["dist/*.js", "!dist/config.js", "!dist/bundle-app.js", "!dist/bundle-vendor.js"]);
    del.sync(["dist/*.html", "!dist/index.html"]);
    del.sync(["tmp/**/*"]);
    //del.sync(["dist/jspm_packages/github/**/*"]);
    //del.sync(["dist/jspm_packages/npm/**/*"]);
});

gulp.task("release", function () {
    sequence('clear-all',
            ['copy-lib', 'copy-config'],
             'copy-template',
             'copy-resource',
             'build',
             'backup-config',
             'bundle',
             'copy-config',
             'restore-config',
             'copy-index',
             'clean-up');
});

gulp.task("build-only", function () {
    sequence('clear-all',
            ['copy-lib', 'copy-index', 'copy-config', 'copy-bundle'],
             'copy-template',
             'copy-resource',
             'build');
});

gulp.task('start', function() {
  gulp.src('./')
  .pipe(webserver({
      host: "0.0.0.0",
      port: 9090
    }));
});

gulp.task('default', ['rebuild']);
