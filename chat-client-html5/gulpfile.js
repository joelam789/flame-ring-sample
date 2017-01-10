

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

gulp.task('copy-module-config', function () {
    return gulp.src(["./config.js"]).pipe(gulp.dest("./dist/"));
});

gulp.task('backup-module-config', function () {
    return gulp.src(["./config.js"]).pipe(gulp.dest("./tmp/"));
});

gulp.task('restore-module-config', function () {
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

gulp.task("compile", () => {
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
    return gulp.watch(["./index.html", "./app-config.json", "./src/*.*", "./res/**/*"],
                      ["build-main"]);
});

gulp.task("build-main", function () {
    sequence('copy-index',
             'copy-template',
             'copy-resource',
             'compile',
             'apply-app-config');
});

gulp.task("build-and-watch", function () {
    sequence('clear-all',
            ['copy-lib', 'copy-index', 'copy-module-config', 'copy-bundle'],
             'copy-template',
             'copy-resource',
             'compile',
             'apply-app-config',
             'watch');
});

gulp.task("before-bundle", function () {
    del.sync(["./dist/bundle-app.js"]);
    del.sync(["./dist/bundle-vendor.js"]);

    let appConfig = JSON.parse(fs.readFileSync('./app-config.json', 'utf8'));
    if (appConfig.rootDir != undefined && appConfig.rootDir.length > 0 && appConfig.rootDir != "dist") {
        return gulp.src([
            "./dist/*.js", 
            "./dist/*.html", 
            "./dist/*.css"])
        .pipe(gulp.dest("./" + appConfig.rootDir + "/"));
    }
});

gulp.task("bundle", function () {
    
    let appConfig = JSON.parse(fs.readFileSync('./app-config.json', 'utf8'));
    let bundleConfig = JSON.parse(fs.readFileSync('./bundle.json', 'utf8'));

    if (appConfig.rootDir != undefined && appConfig.rootDir.length > 0) {
        bundleConfig.bundles['dist/bundle-app'].includes = [
            "[" + appConfig.rootDir + "/*.js]", 
            appConfig.rootDir + "/*.html!text",
            appConfig.rootDir + "/*.css!text"
        ];
    }

    return abundle(bundleConfig);
});

gulp.task("after-bundle", function () {
    let appConfig = JSON.parse(fs.readFileSync('./app-config.json', 'utf8'));
    if (appConfig.rootDir != undefined && appConfig.rootDir.length > 0 && appConfig.rootDir != "dist") {
        del.sync(["./" + appConfig.rootDir.split('/')[0] + "/**/*"]);
        del.sync(["./" + appConfig.rootDir.split('/')[0]]);
    }
});

gulp.task("apply-app-config", function () {
    let appConfig = JSON.parse(fs.readFileSync('./app-config.json', 'utf8'));
    let configCode = "window.mainAppConfig = JSON.parse('" + JSON.stringify(appConfig) + "');";
    fs.writeFileSync('./dist/js/app-config.js', configCode, 'utf8');
    
});

gulp.task('clean-up', function () {
    del.sync(["dist/*.js.map"]);
    del.sync(["dist/*.js", "!dist/config.js", "!dist/bundle-app.js", "!dist/bundle-vendor.js"]);
    del.sync(["dist/*.html", "!dist/index.html"]);
    del.sync(["tmp/**/*"]);
    del.sync(["./tmp"]);
    del.sync(["dist/jspm_packages/github/**/*"]);
    del.sync(["dist/jspm_packages/npm/**/*"]);
});

gulp.task("release", function () {
    sequence('clear-all',
            ['copy-lib', 'copy-module-config'],
             'copy-template',
             'copy-resource',
             'compile',
             'backup-module-config',
             'before-bundle',
             'bundle',
             'copy-module-config',
             'after-bundle',
             'restore-module-config',
             'apply-app-config',
             'copy-index',
             'clean-up');
});

gulp.task("build-only", function () {
    sequence('clear-all',
            ['copy-lib', 'copy-index', 'copy-module-config', 'copy-bundle'],
             'copy-template',
             'copy-resource',
             'compile',
             'apply-app-config');
});

gulp.task('start', function() {
  gulp.src('./')
  .pipe(webserver({
      host: "0.0.0.0",
      port: 9090
    }));
});

gulp.task('default', ['build-and-watch']);
